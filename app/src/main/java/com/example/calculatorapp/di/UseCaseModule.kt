package com.example.calculatorapp.di

import com.example.domain.usecase.GetHistoryUseCase
import com.example.domain.usecase.ReqDeleteAllHistoryUseCase
import com.example.domain.usecase.ReqInsertHistoryUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetHistoryUseCase(get()) }
    factory { ReqDeleteAllHistoryUseCase(get()) }
    factory { ReqInsertHistoryUseCase(get()) }
}