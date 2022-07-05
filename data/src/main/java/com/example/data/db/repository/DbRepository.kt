package com.example.data.db.repository

import com.example.data.db.entity.History
import kotlinx.coroutines.flow.Flow

interface DbRepository {
    fun getAll(): Flow<List<History>>
    suspend fun insertHistory(history: History)
    suspend fun deleteAll()
}