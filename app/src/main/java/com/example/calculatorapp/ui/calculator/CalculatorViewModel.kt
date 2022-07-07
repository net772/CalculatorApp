package com.example.calculatorapp.ui.calculator

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.calculatorapp.ui.base.BaseViewModel
import com.example.data.db.entity.History
import com.example.domain.usecase.ReqInsertHistoryUseCase
import kotlinx.coroutines.launch

class CalculatorViewModel(
    app: Application,
    private val insertHistoryUseCase: ReqInsertHistoryUseCase,
) : BaseViewModel(app) {

    fun insertHistory(history: History) {
        viewModelScope.launch {
            insertHistoryUseCase.invoke(history)
        }
    }
}