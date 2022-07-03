package com.example.calculator.state

sealed class ResultState<out T> {

    object UnInitialize : ResultState<Nothing>()

    object Loading : ResultState<Nothing>()

    data class Success<T>(val data: T) : ResultState<T>()

    data class Error(val error: Throwable) : ResultState<Nothing>()

    object Finish : ResultState<Nothing>()

}