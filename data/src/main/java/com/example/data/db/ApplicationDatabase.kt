package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.db.dao.HistoryDao
import com.example.data.db.entity.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "ApplicationDataBase.db"
    }

    abstract fun historyDao(): HistoryDao
}