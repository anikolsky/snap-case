package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.data.page.PageFactory
import com.omtorney.snapcase.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject

class SearchCaseUseCase @Inject constructor(private val repository: Repository) {

    suspend fun execute(court: Court, query: String): List<Case> {
        var sideName = ""
        var caseNumber = ""
        if (query.contains("[а-яА-Яa-zA-Z]".toRegex())) sideName = query
        else caseNumber = query
        val searchUrl = court.getSearchQuery(withContext(Dispatchers.IO) {
            URLEncoder.encode(sideName, "cp1251")
        }, URLEncoder.encode(caseNumber, "cp1251"))

        val html = withContext(Dispatchers.IO) {
            repository.getHtmlData(searchUrl)
        }
        val page = PageFactory().createPage(court)
        return page.extractSearchResult(html, court)
    }
}