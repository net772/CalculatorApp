package com.example.calculatorapp.di

import com.example.data.db.repository.DbRepository
import com.example.data.db.repository.DbRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<DbRepository> {  DbRepositoryImpl(get() ) }
}