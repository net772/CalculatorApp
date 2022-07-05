package com.example.domain.usecase

import com.example.data.db.repository.DbRepository
import com.example.domain.UseCase

class ReqDeleteAllHistoryUseCase(
    private val dbRepository: DbRepository
) : UseCase {
    suspend operator fun invoke() = dbRepository.deleteAll()
}