package com.omtorney.snapcase.data.page

import android.util.Log
import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.data.model.CaseProcess
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class NoMskPage : Page {

    override fun extractSchedule(html: String, court: Court): List<Case> {
        val caseList = mutableListOf<Case>()
        val document = Jsoup.parse(html)
        val mainCard = document.select("div[id=content]")
        val searchResult = mainCard.select("table[id=tablcont]").first()
        val resultLines = searchResult!!.getElementsByAttributeValue("valign", "top")
        for (resultLine in resultLines) {
            val lineElements = resultLine.select("td")
            val info = lineElements[4].text()
            val case = Case().apply {
                url = court.basicUrl + lineElements[1].child(0).attr("href")
                number = getCaseNumber(lineElements[1].text())
                hearingDateTime = lineElements[2].text()
                category = getCaseCategory(info)
                participants = "${getCasePlaintiff(info)}\n${getCaseDefendant(info)}"
                judge = lineElements[5].text()
                result = lineElements[6].text()
                actTextUrl = getCaseActUrl(lineElements[7], court)
            }
            caseList.add(case)
        }
        return caseList
    }

    override fun extractSearchResult(html: String, court: Court): List<Case> {
        val caseList = mutableListOf<Case>()
        val document = Jsoup.parse(html)
        val mainCard = document.select("div[id=content]")
        val searchResult = mainCard.select("table[id=tablcont]").first()
        val resultLines = searchResult!!.getElementsByAttributeValue("valign", "top")
        for (resultLine in resultLines) {
            val resultElement = resultLine.select("td")
            val info = resultElement[2].text()
            val case = Case().apply {
                url = court.basicUrl + resultElement.first()!!.child(0).attr("href")
                number = getCaseNumber(resultElement[0].text())
                receiptDate = resultElement[1].text()
                category = getCaseCategory(info)
                participants = "${getCasePlaintiff(info)} ${getCaseDefendant(info)}"
                judge = resultElement[3].text()
                actDateTime = resultElement[4].text()
                result = resultElement[5].text()
                actDateForce = resultElement[6].text()
                actTextUrl = getCaseActUrl(resultElement[7], court)
            }
            caseList.add(case)
        }
        return caseList
    }

    override fun fillCase(case: Case, court: Court): Case {
        val page = Jsoup.connect(case.url).get()
        val mainCard = page.select("div[id=content]")
        val tabsContent = mainCard.select("table[id=tablcont]")
        val tabTitles = tabsContent.select("th")

        for (tab in tabsContent.indices) {
            val tableLines = tabsContent[tab].select("tr")
            repeat(2) { tableLines.removeFirstOrNull() }
            for (line in tableLines.indices) {

                if (tabTitles[tab].text() == "ДЕЛО") {
                    val caseLineElements = tableLines[line].select("td")
                    if (caseLineElements[0].text() == "Уникальный идентификатор дела") {
                        case.uid = caseLineElements[1].text()
                    }
                }

                if (tabTitles[tab].text() == "ДВИЖЕНИЕ ДЕЛА") {
                    val processLineElements = tableLines[line].select("td")
                    for (elem in processLineElements.indices) {
                        if (processLineElements[elem].text().isEmpty())
                            processLineElements[elem].text("")
                    }
                    case.process.add(CaseProcess(
                            event = processLineElements[0].text(),
                            date = processLineElements[1].text(),
                            time = processLineElements[2].text(),
                            result = processLineElements[4].text(),
                            cause = processLineElements[5].text(),
                            dateOfPublishing = processLineElements[7].text()
                        ))
                    Log.d("TESTLOG", "(nomskpage) process: ${case.process}")
                }

                if (tabTitles[tab].text() == "СТОРОНЫ ПО ДЕЛУ (ТРЕТЬИ ЛИЦА)") {
                    val sidesLineElements = tableLines[line].select("td")
                    if (sidesLineElements[0].text().contains("ЗАИНТЕРЕСОВАННОЕ")
                        || sidesLineElements[0].text().contains("ТРЕТЬЕ"))
                        case.participants.plus(" / ИНЫЕ ЛИЦА: ${sidesLineElements[1].text()}")
                }

                if (tabTitles[tab].text() == "ОБЖАЛОВАНИЕ РЕШЕНИЙ, ОПРЕДЕЛЕНИЙ (ПОСТ.)") {
                    val appealsLineElements = tableLines[line].select("td")
                    for (elem in appealsLineElements.indices) {
                        if (appealsLineElements[elem].text().isEmpty())
                            appealsLineElements[elem].text("")
                    }
                    // Получаем только последнее обжалование, т.к. я не знаю как записывать все обжалования в разные ветки
                    case.appeal[appealsLineElements[0].text()] =
                        appealsLineElements[appealsLineElements.size - 1].text()
                    Log.d("TESTLOG", "(nomskpage) appeal: ${case.appeal}")
                }
            }
        }
        return case
    }

    override fun extractActText(url: String): ArrayList<String> {
        val act = arrayListOf<String>()
        val page = Jsoup.connect(url).get()
        val mainSection = page.getElementById("modSdpContent")
        val paragraph = mainSection?.getElementsByTag("p")
        paragraph?.forEach {
            act.add(it.text())
        }
        return act
    }

    override fun getCasePlaintiff(info: String): String {
        return if (info.contains("ИСТЕЦ") && !info.contains("ОТВЕТЧИК"))
            info.substring(info.indexOf("ИСТЕЦ"))
        else if (info.contains("ИСТЕЦ"))
            info.substring(info.indexOf("ИСТЕЦ"), info.indexOf("ОТВЕТЧИК") - 1)
        else ""
    }

    override fun getCaseDefendant(info: String): String {
        return if (info.contains("ОТВЕТЧИК")) info.substring(info.indexOf("ОТВЕТЧИК"))
        else if (info.contains("ПРАВОНАРУШЕНИЕ")) {
            if (info.contains("-")) {
                info.substring(info.indexOf(":") + 1, info.indexOfLast { it == '-' } - 1)
            } else info.substring(info.indexOf(":") + 1)
        } else ""
    }

    override fun getCaseCategory(info: String): String {
        return if (info.contains("КАТЕГОРИЯ") && !info.contains("ИСТЕЦ"))
            info.substring(info.indexOf("КАТЕГОРИЯ"))
        else if (info.contains("КАТЕГОРИЯ"))
            info.substring(info.indexOf("КАТЕГОРИЯ") + 11, info.indexOf("ИСТЕЦ"))
        else if (info.contains("ПРАВОНАРУШЕНИЕ"))
            info.substring(info.indexOf("ПРАВОНАРУШЕНИЕ"), info.indexOf(":")) +
                    ": " + info.substring(info.indexOfLast { it == '-' } + 1)
        else ""
    }

    override fun getCaseNumber(numberString: String): String {
        return if (numberString.contains("~"))
            numberString.substring(0, numberString.indexOf("~") - 1)
        else numberString
    }

    override fun getCaseActUrl(element: Element, court: Court): String {
        val linkToAct = element.select("a").attr("href")
        return if (linkToAct.isNotEmpty()) court.basicUrl + linkToAct else ""
    }

    override fun paginator(page: Document, searchUrl: String, court: Court): List<String> {
        val pagesUrls: List<String>

        // TODO: загружать следующие страницы только по запросу

        /** Получить предыдущий элемент того же уровня (из-за особенностей верстки сайта) */
        val pageList = page.select("table[id=tablcont]").first()!!.previousElementSibling()
        val ahrefs = pageList!!.getElementsByTag("a")

        /** Если страниц несколько */
        if (ahrefs.size > 0) {

            /** Получить ссылку на последнюю страницу */
            val lastPageUrl = ahrefs[ahrefs.size - 1]?.attr("href")
            val pageVarIndex = lastPageUrl!!.indexOf("page")

            /** Получить общее число страниц */
            val pagesTotal = lastPageUrl
                .substring(pageVarIndex + 5, lastPageUrl.indexOf('&', pageVarIndex))
                .toInt()

            /** Добавить все ссылки в массив */
            pagesUrls = List(pagesTotal) { index ->
                court.basicUrl + lastPageUrl
                    .removeRange(0, 1)
                    .replace("page=$pagesTotal", "page=${index + 1}")
            }

            /** Если страница одна */
        } else pagesUrls = arrayListOf(searchUrl)
        return pagesUrls
    }
}