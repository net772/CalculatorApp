package com.example.calculatorapp.di

import com.example.calculatorapp.ui.calculator.CalculatorViewModel
import com.example.calculatorapp.ui.main.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(androidApplication()) }
    viewModel { CalculatorViewModel(androidApplication(), get(), get(), get()) }
}