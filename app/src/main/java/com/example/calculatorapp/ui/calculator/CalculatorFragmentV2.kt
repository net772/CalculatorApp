package com.example.calculatorapp.ui.calculator

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.calculatorapp.R
import com.example.calculatorapp.databinding.FragmentCalculatorBinding
import com.example.calculatorapp.ui.base.BaseFragment
import com.example.calculatorapp.utility.extension.throttleFirstClick
import com.example.data.db.entity.History
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.NumberFormatException
import java.util.*

class CalculatorFragmentV2 : BaseFragment<FragmentCalculatorBinding>() {
    companion object {
        const val TAG = "CalculatorFragment"
        fun newInstance() = CalculatorFragmentV2()
    }

    private val viewModel: CalculatorViewModel by viewModel()

    private var isOperator = false
    private var isBracket = false

    override fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalculatorBinding.inflate(inflater, container, false)

    override fun initFragment() {
        onClickEvent()
    }

    private fun onClickEvent() = with(binding) {
        throttleFirstClick(button0) { numberButtonClicked("0") }
        throttleFirstClick(button1) { numberButtonClicked("1") }
        throttleFirstClick(button2) { numberButtonClicked("2") }
        throttleFirstClick(button3) { numberButtonClicked("3") }
        throttleFirstClick(button4) { numberButtonClicked("4") }
        throttleFirstClick(button5) { numberButtonClicked("5") }
        throttleFirstClick(button6) { numberButtonClicked("6") }
        throttleFirstClick(button7) { numberButtonClicked("7") }
        throttleFirstClick(button8) { numberButtonClicked("8") }
        throttleFirstClick(button9) { numberButtonClicked("9") }
        throttleFirstClick(buttonPlus) { operatorButtonClicked("+") }
        throttleFirstClick(buttonMinus) { operatorButtonClicked("-") }
        throttleFirstClick(buttonMulti) { operatorButtonClicked("*") }
        throttleFirstClick(buttonDivider) { operatorButtonClicked("/") }
        throttleFirstClick(buttonModulo) {  }
        throttleFirstClick(bracket) { bracketButtonClicked() }
        throttleFirstClick(resultButton) { resultButtonClicked() }
        throttleFirstClick(clearButton) { clearExpressionText() }
    }

    private fun numberButtonClicked(number: String) = with(binding) {
        if (isOperator) expressionTextView.append(" ")
        if (expressionTextView.text.takeLast(1).toString() == ")") expressionTextView.append(" ")
        if (expressionTextView.text.takeLast(1).toString() == "(") expressionTextView.append(" ")
        val expressionText = expressionTextView.text.split(" ")

        if (expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(requireContext(), "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)

        if (!isBracket) resultTextView.text = calc(test(expressionTextView.text.toString())).toString()

        isOperator = false
    }

    private fun bracketButtonClicked() = with(binding) {
        isBracket = !isBracket
        if (isBracket) {
            val text = if (expressionTextView.text.isEmpty()) "( " else " ("
            expressionTextView.append(text)
        }
        else {
            expressionTextView.append(" )")
            if (expressionTextView.text.takeLast(1).toString() == ")") resultTextView.text = calc(test(expressionTextView.text.toString())).toString()
        }
        isOperator = false
    }

    private fun operatorButtonClicked(operator: String) = with(binding) {
        if (expressionTextView.text.isEmpty()) return
        if(expressionTextView.text.takeLast(1).toString() == "(" || expressionTextView.text.takeLast(2).toString() == "(") return
        when {
            isOperator -> {
                val text = expressionTextView.text.toString()
                expressionTextView.text = getString(R.string.replace_operation, text.dropLast(1) + operator)
                operatorOverlapStringBuilder(expressionTextView.text.toString())
                return
            }

            else -> expressionTextView.append(" $operator")
        }

        setSpannableStringBuilder(mutableListOf(expressionTextView.text.length - 1))
        isOperator = true
    }

    private fun operatorOverlapStringBuilder(input: String) = with(binding) {
        val opList = listOf('+', '-', '*', '/')
        val expressionList = input.toList()
        val idxList = mutableListOf<Int>()

        expressionList.forEachIndexed { idx, exp ->
            opList.forEach { if(exp == it)  idxList.add(idx) }
        }
        setSpannableStringBuilder(idxList)
    }

    private fun setSpannableStringBuilder(idxList: MutableList<Int>) = with(binding) {
        val ssb = SpannableStringBuilder(expressionTextView.text)
        for (startIdx in idxList) {
            ssb.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.green)),
                startIdx,
                startIdx + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        expressionTextView.text = ssb
    }

    private fun test(input: String) : List<String> {
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

    private fun calc(stackExpressionStr: List<String>) : Double {
        val numberStack = Stack<Double>()

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

    private fun resultButtonClicked() = with(binding) {
//        val expressionTexts = expressionTextView.text.split(" ")
//        val resultPossible = expressionTexts.size >= 3 && expressionTexts.size % 2 != 0
//
//        if (expressionTextView.text.isEmpty() || !resultPossible) {
//            Toast.makeText(requireContext(), "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (expressionTexts[0].isNumber().not() && expressionTexts[0] != "(") {
//            Toast.makeText(requireContext(), "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
//            return
//        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calc(test(expressionTextView.text.toString())).toString()

//        viewModel.insertHistory(History(null, expressionText, resultText))
        resultTextView.text = resultText
//        Toast.makeText(requireContext(), "수식을 저장하였습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun clearExpressionText() = with(binding) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        isBracket = false
    }
}

enum class OperatorPriorityWithParentheses(_priority: Int, _operatorList: List<String>) {
    PARENTHESES(0, listOf("(")),
    PLUS_MINUS(1, listOf("+", "-")),
    MULTI_DIVIDE(2, listOf("*", "/"));

    private var priority: Int = _priority
    private var operatorList: List<String> = _operatorList

    companion object {
        fun findPriority(operator: String): OperatorPriorityWithParentheses {
            val op = values().filter { operatorPriority ->
                operatorPriority.hasOperation(operator)
            }
            return op.first()
        }
    }

    private fun hasOperation(operator: String) : Boolean {
        return operatorList.contains(operator)
    }

    fun getPriority() = priority
}

