package com.example.calculatorapp.ui.calculator

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.calculatorapp.ui.base.BaseViewModel
import com.example.data.db.entity.History
import com.example.domain.usecase.GetHistoryUseCase
import com.example.domain.usecase.ReqDeleteAllHistoryUseCase
import com.example.domain.usecase.ReqInsertHistoryUseCase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CalculatorViewModel(
    app: Application,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val insertHistoryUseCase: ReqInsertHistoryUseCase,
    private val deleteAllHistoryUseCase: ReqDeleteAllHistoryUseCase
) : BaseViewModel(app) {


    fun getAllHistory() {
        viewModelScope.launch {
            getHistoryUseCase.invoke().firstOrNull()
        }
    }

    fun insertHistory(history: History) {
        viewModelScope.launch {
            insertHistoryUseCase.invoke(history)
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch {
            deleteAllHistoryUseCase.invoke()
        }
    }
}