package com.example.calculatorapp.utility.state

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> mutableResultState(
    uiState: ResultUiState<T> = ResultUiState.UnInitialize
): MutableStateFlow<ResultUiState<T>> = MutableStateFlow(uiState)

sealed class ResultUiState<out T> {

    object UnInitialize : ResultUiState<Nothing>()

    /**
     * API 호출 및 결과까지 걸리는 상태
     * */
    object Loading : ResultUiState<Nothing>()

    /**
     * API 호출 및 결과 성공 상태
     *  */
    data class Success<T>(val data: T) : ResultUiState<T>()

    /**
     * API 호출 및 결과를 받기 전까지의 과정중 예외가 발생했을때의 상태
     * */
    data class Error(val error: Throwable) : ResultUiState<Nothing>()

    /**
     * 모든 호출 결과 상태(Success, Error)가 되고난 후 최종적인 상태
     *
     * 보통 이 상태에서 로딩처리를 종료시킵니다.
     * */
    object Finish : ResultUiState<Nothing>()

}