package com.omtorney.snapcase.domain.usecase

import com.omtorney.snapcase.data.court.Court
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.data.page.PageFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FillCaseUseCase @Inject constructor() {

    suspend fun execute(case: Case, court: Court): Case {
        val page = PageFactory().createPage(court)
        val caseFull = withContext(Dispatchers.IO) {
            page.fillCase(case, court)
        }
        return caseFull
    }
}