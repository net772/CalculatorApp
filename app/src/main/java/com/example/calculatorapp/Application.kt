package com.example.calculatorapp

import android.app.Application
import android.content.Context
import com.example.calculatorapp.di.coroutineModule
import com.example.calculatorapp.di.dataBaseModule
import com.example.calculatorapp.di.useCaseModule
import com.example.calculatorapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Application: Application() {

    companion object {
        private var instance: Application? = null
        val applicationContext: Context get() = instance!!.applicationContext
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Application)
            modules(
                coroutineModule,
                useCaseModule,
                viewModelModule,
                dataBaseModule
            )
        }
    }
}