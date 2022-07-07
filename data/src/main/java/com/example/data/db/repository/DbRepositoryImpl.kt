package com.example.data.db.repository

import com.example.data.db.dao.HistoryDao
import com.example.data.db.entity.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DbRepositoryImpl(
    private val historyDao: HistoryDao
) : DbRepository{
    override fun getAll(): Flow<List<History>> = flow {
        emit(historyDao.getAll())
    }

    override suspend fun insertHistory(history: History) = historyDao.insertHistory(history)

    override suspend fun deleteAll() = historyDao.deleteAll()
}