package com.omtorney.snapcase.data.local

import com.omtorney.snapcase.data.model.Case

interface LocalDataSource {

    suspend fun deleteFavorite(case: Case)
    suspend fun addFavorite(case: Case)
}