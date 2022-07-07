package com.example.calculatorapp.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculatorapp.utility.KoinConstants
import com.example.calculatorapp.utility.state.ResultUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

abstract class BaseViewModel(
    private val app: Application
) : AndroidViewModel(app), KoinComponent {
    private val ioDispatcher: CoroutineDispatcher by inject(named(KoinConstants.DISPATCHER_IO))


    protected fun <T> Flow<T>.onState(collect: (ResultUiState<T>) -> Unit) {
        flowOn(ioDispatcher)
            .onCompletion { collect(ResultUiState.Finish) }
            .onStart { collect(ResultUiState.Loading) }
            .catch { collect(ResultUiState.Error(it)) }
            .onEach { collect(ResultUiState.Success(it)) }
            .launchIn(viewModelScope)
    }
}