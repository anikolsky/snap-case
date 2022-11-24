package com.omtorney.snapcase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omtorney.snapcase.data.model.Case

@Database(entities = [Case::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun caseDao(): CaseDao
}