package com.omtorney.snapcase.data.page

import com.omtorney.snapcase.data.court.Court

class PageFactory {

    fun createPage(court: Court): Page {
        return when(court.type) {
            PageType.NoMsk -> NoMskPage()
            PageType.Msk -> MskPage()
        }
    }
}