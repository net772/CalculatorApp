package com.example.calculatorapp.ui.calculator

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.calculatorapp.state.mutableResultState
import com.example.calculatorapp.ui.base.BaseViewModel
import com.example.data.db.entity.History
import com.example.domain.usecase.ReqInsertHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.util.*

class CalculatorViewModel(
    app: Application,
    private val insertHistoryUseCase: ReqInsertHistoryUseCase,
) : BaseViewModel(app) {

    fun insertHistory(history: History) {
        viewModelScope.launch {
            insertHistoryUseCase.invoke(history)
        }
    }

    fun checkLastStrNumber(lastStr: String) : Boolean {
        return try {
            lastStr.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun setStackExpression(input: String) : List<String> {
        val expressionList = input.split(" ")
        var expressionText = ""
        val operatorStack = Stack<String>()

        for (exp in expressionList) {
            try {
                val number = exp.toDouble()
                expressionText += "$number "
            } catch (e: NumberFormatException) {
                if (exp == "(") operatorStack.push("(")
                else if (exp == ")") {
                    while (!operatorStack.peek().equals("(")) expressionText += operatorStack.pop() + " "
                    operatorStack.pop()
                } else {
                    val priority = OperatorPriorityWithParentheses.findPriority(exp)
                    while (!operatorStack.isEmpty()) {
                        val endInStack = operatorStack.peek()
                        if (priority.getPriority() <= OperatorPriorityWithParentheses.findPriority(endInStack).getPriority()) { // 현재 우선순위가 더 높으면
                            expressionText += operatorStack.pop() + " "
                        }
                        else break
                    }
                    operatorStack.push(exp)
                }
            }
        }

        while (!operatorStack.isEmpty()) expressionText += operatorStack.pop() + " ";

        return expressionText.trim().split(" ")
    }

    fun calc(expressionText: String) : Double {
        val numberStack = Stack<Double>()
        val stackExpressionStr = setStackExpression(expressionText)
        for (exp in stackExpressionStr) {
            try {
                val number = exp.toDouble()
                numberStack.push(number)
            }  catch (e: NumberFormatException) {
                if (numberStack.size < 2) return 0.0
                val num1 = numberStack.pop()
                val num2 = numberStack.pop()

                when (exp) {
                    "+" -> numberStack.push(num2 + num1)
                    "-" -> numberStack.push(num2 - num1)
                    "*" -> numberStack.push(num2 * num1)
                    "/" -> numberStack.push(num2 / num1)
                }
            }
        }
        return numberStack.pop()
    }
}