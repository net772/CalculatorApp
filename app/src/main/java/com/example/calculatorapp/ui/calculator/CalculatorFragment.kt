package com.example.calculatorapp.ui.calculator

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.calculatorapp.R
import com.example.calculatorapp.databinding.FragmentCalculatorBinding
import com.example.calculatorapp.ui.base.BaseFragment
import com.example.calculatorapp.utility.extension.throttleFirstClick
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : BaseFragment<FragmentCalculatorBinding>() {
    companion object {
        const val TAG = "CalculatorFragment"
        fun newInstance() = CalculatorFragment()
    }

    private val viewModel: CalculatorViewModel by viewModel()

    private var isOperator = false

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
        throttleFirstClick(buttonModulo) { operatorButtonClicked("%") }
    }

    private fun numberButtonClicked(number: String) = with(binding) {
        if (isOperator) expressionTextView.append(" ")

        val expressionText = expressionTextView.text.split(" ")

        if (expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(requireContext(), "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)
        resultTextView.text = calc(expressionTextView.text.toString()).toString()

        isOperator = false
    }

    private fun operatorButtonClicked(operator: String) = with(binding) {
        if (expressionTextView.text.isEmpty()) return


        when {
            isOperator -> {
                val text = expressionTextView.text.toString()
                expressionTextView.text = getString(R.string.replace_operation, text.dropLast(1), operator)
            }

            else -> expressionTextView.append(" $operator")
        }

        setSpannableStringBuilder()
        isOperator = true
    }

    private fun setSpannableStringBuilder() = with(binding) {
        val ssb = SpannableStringBuilder(expressionTextView.text)
        ssb.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.green)),
            expressionTextView.text.length - 1,
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        expressionTextView.text = ssb
    }

    private fun calc(input: String) : Int {
        when {
            input.contains("+") -> {
                val idx = input.indexOf("+")
                return calc(input.substring(0, idx)) + calc(input.substring(idx + 1))
            }
            input.contains("-") -> {
                val idx = input.indexOf("-")
                return calc(input.substring(0, idx)) - calc(input.substring(idx + 1))
            }
            input.contains("*") -> {
                val idx = input.indexOf("*")
                return calc(input.substring(0, idx)) * calc(input.substring(idx + 1))
            }
            input.contains("/") -> {
                val idx = input.indexOf("/")
                return calc(input.substring(0, idx)) / calc(input.substring(idx + 1))
            }
            input.contains("%") -> {
                val idx = input.indexOf("%")
                return calc(input.substring(0, idx)) % calc(input.substring(idx + 1))
            }
        }

        val data = input.trim()
        if (data.isEmpty()) return 0
        return data.toInt()
    }
}