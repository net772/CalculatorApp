package com.example.calculatorapp.ui.history

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.calculatorapp.ui.base.BaseViewModel
import com.example.calculatorapp.utility.state.mutableResultState
import com.example.data.db.entity.History
import com.example.domain.usecase.GetHistoryUseCase
import com.example.domain.usecase.ReqDeleteAllHistoryUseCase
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    app: Application,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val deleteAllHistoryUseCase: ReqDeleteAllHistoryUseCase
) : BaseViewModel(app) {

    private val _historyData = mutableResultState<List<History>>()
    val historyData = _historyData.asStateFlow()

    fun getAllHistory() {
         getHistoryUseCase.invoke().onState {
             _historyData.value = it
         }
    }

    fun deleteAllHistory() {
        viewModelScope.launch {
            deleteAllHistoryUseCase.invoke()
        }
    }
}