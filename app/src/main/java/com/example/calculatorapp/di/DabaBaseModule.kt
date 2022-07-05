package com.example.calculatorapp.di

import com.example.data.db.provideDB
import com.example.data.db.provideHistoryDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataBaseModule = module {
    single { provideDB(androidApplication()) }
    single { provideHistoryDao(get()) }

}
