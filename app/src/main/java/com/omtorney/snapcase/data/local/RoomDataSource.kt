package com.omtorney.snapcase.data.local

import com.omtorney.snapcase.data.model.Case

class RoomDataSource(private val caseDao: CaseDao) : LocalDataSource {

    override suspend fun deleteFavorite(case: Case) {
        caseDao.delete(case)
    }

    override suspend fun addFavorite(case: Case) {
        caseDao.insert(case)
    }
}