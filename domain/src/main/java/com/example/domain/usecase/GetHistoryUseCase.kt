package com.example.domain.usecase

import com.example.data.db.repository.DbRepository
import com.example.domain.UseCase

class GetHistoryUseCase(
    private val dbRepository: DbRepository
) : UseCase {
    operator fun invoke() = dbRepository.getAll()
}