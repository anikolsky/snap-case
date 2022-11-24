package com.omtorney.snapcase.data.remote

import org.jsoup.Jsoup

class JsoupDataSource : RemoteDataSource {

    override suspend fun getData(url: String): String {
        return Jsoup.connect(url).get().toString()
    }
}