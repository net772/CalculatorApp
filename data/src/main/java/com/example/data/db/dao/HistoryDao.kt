package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.db.entity.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): Flow<List<History>>

    @Insert
    suspend fun insertHistory(history: History)

    @Query("DELETE FROM history")
    suspend fun deleteAll()
}