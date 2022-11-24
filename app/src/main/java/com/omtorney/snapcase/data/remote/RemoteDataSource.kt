package com.omtorney.snapcase.data.remote

interface RemoteDataSource {

    suspend fun getData(url: String): String
}