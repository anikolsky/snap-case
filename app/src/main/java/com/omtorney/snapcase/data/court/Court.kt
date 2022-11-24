package com.omtorney.snapcase.data.court

import com.omtorney.snapcase.data.page.PageType

interface Court {

    val type: PageType
    val basicUrl: String

    fun getScheduleQuery(date: String): String
    fun getSearchQuery(sideName: String, caseNumber: String): String
}