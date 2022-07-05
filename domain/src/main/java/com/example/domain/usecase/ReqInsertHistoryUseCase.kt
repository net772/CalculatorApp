package com.example.domain.usecase

import com.example.data.db.entity.History
import com.example.data.db.repository.DbRepository
import com.example.domain.UseCase

class ReqInsertHistoryUseCase(
    private val dbRepository: DbRepository
) : UseCase {
    suspend operator fun invoke(history: History) = dbRepository.insertHistory(history)
}