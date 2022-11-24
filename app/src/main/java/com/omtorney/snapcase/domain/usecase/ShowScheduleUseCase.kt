package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.data.page.PageFactory
import com.omtorney.snapcase.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShowScheduleUseCase @Inject constructor(private val repository: Repository) {

    suspend fun execute(court: Court, date: String): List<Case> {
        val html = withContext(Dispatchers.IO) {
            repository.getHtmlData(court.getScheduleQuery(date))
        }
        val page = PageFactory().createPage(court)
        return page.extractSchedule(html, court)
    }
}