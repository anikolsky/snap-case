package com.omtorney.snapcase.data.page

sealed class PageType {
    object NoMsk : PageType()
    object Msk : PageType()
}
