package com.example.calculatorapp.state

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow

fun <T> mutableResultState(
    uiState: ResultUiState<T> = ResultUiState.UnInitialize
): MutableStateFlow<ResultUiState<T>> = MutableStateFlow(uiState)

fun <T> mutableResultLive(
    uiState: ResultUiState<T> = ResultUiState.UnInitialize
): MutableLiveData<ResultUiState<T>> = MutableLiveData(uiState)

sealed class ResultUiState<out T> {

    object UnInitialize : ResultUiState<Nothing>()
    object Loading : ResultUiState<Nothing>()
    data class Success<T>(val data: T) : ResultUiState<T>()
    data class Error(val error: Throwable) : ResultUiState<Nothing>()
    object Finish : ResultUiState<Nothing>()

}