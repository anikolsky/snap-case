package com.omtorney.snapcase.data

import com.omtorney.snapcase.data.local.LocalDataSource
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.data.remote.RemoteDataSource
import com.omtorney.snapcase.domain.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    override suspend fun getHtmlData(url: String): String {
        return remoteDataSource.getData(url)
    }

    override suspend fun deleteFavorite(case: Case) {
        localDataSource.deleteFavorite(case)
    }

    override suspend fun addFavorite(case: Case) {
        localDataSource.addFavorite(case)
    }
}