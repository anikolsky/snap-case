package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.page.PageFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShowActTextUseCase @Inject constructor() {

    suspend fun execute(court: Court, url: String): ArrayList<String> {
        val page = PageFactory().createPage(court)
        val actText = withContext(Dispatchers.IO) {
            page.extractActText(url)
        }
        return actText
    }
}