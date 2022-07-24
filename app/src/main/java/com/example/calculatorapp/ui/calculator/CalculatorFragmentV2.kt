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
        if (expressionTextView.text.isEmpty() && number == "0") {
            Toast.makeText(requireContext(), "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (expressionTextView.text.isEmpty()) expressionTextView.append(number) else expressionTextView.append(" $number")

        val expressionText = expressionTextView.text.toString()
        if (!isBracket) resultTextView.text = viewModel.calc(expressionText).toString()

        isOperator = false
    }

    private fun bracketButtonClicked() = with(binding) {
        isBracket = !isBracket
        if (isBracket) {
            val text = when {
                expressionTextView.text.isEmpty() -> "("
                viewModel.checkLastStrNumber(expressionTextView.text.takeLast(1).toString()) -> {
                    " * ("
                }
                else -> " ("
            }

            expressionTextView.append(text)
            operatorOverlapStringBuilder(expressionTextView.text.toString())
        }
        else {
            expressionTextView.append(" )")
            val expressionText = expressionTextView.text.toString()
            if (expressionTextView.text.takeLast(1).toString() == ")") resultTextView.text = viewModel.calc(expressionText).toString()
        }
        isOperator = false
    }

    private fun operatorButtonClicked(operator: String) = with(binding) {
        if (expressionTextView.text.isEmpty()) return
        if (expressionTextView.text.takeLast(1).toString() == "(") return
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
            opList.map {
                if(exp == it)  idxList.add(idx)
            }
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
        val resultText = viewModel.calc(expressionText).toString()

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

enum class OperatorPriorityWithParentheses(private val priority: Int, private val operatorList: List<String>) {
    PARENTHESES(0, listOf("(")),
    PLUS_MINUS(1, listOf("+", "-")),
    MULTI_DIVIDE(2, listOf("*", "/"));


    companion object {
        fun findPriority(operator: String): OperatorPriorityWithParentheses {
            val op = values().filter { operatorPriority ->
                operatorPriority.hasOperation(operator)
            }
            return op.first()
        }
    }

    private fun hasOperation(operator: String) : Boolean {
        return this.operatorList.contains(operator)
    }

    fun getPriority() = this.priority
}

