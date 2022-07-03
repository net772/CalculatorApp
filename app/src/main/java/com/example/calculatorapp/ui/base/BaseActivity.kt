package com.example.calculatorapp.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.calculator.state.ResultState
import com.example.calculatorapp.utility.extension.collect
import kotlinx.coroutines.flow.Flow

abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity(){
    protected val binding: Binding by lazy { createBinding() }
    protected abstract fun createBinding(): Binding
    protected open fun initActivity() = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.lifecycleOwner = this

        initActivity()
    }

    protected fun <T> LiveData<T>.observe(observer: Observer<T>) {
        observe(this@BaseActivity, observer)
    }

    protected fun <T> Flow<T>.onResult(action: (T) -> Unit) {
        collect(lifecycleScope, action)
    }

    protected fun <T> Flow<ResultState<T>>.onUiState(
        loading: () -> Unit = {},
        success: (T) -> Unit = {},
        error: (Throwable) -> Unit = {},
        finish: () -> Unit = {}
    ) {
        onResult { state ->
            when (state) {
                ResultState.Loading -> loading()
                is ResultState.Success -> success(state.data)
                is ResultState.Error -> error(state.error)
                ResultState.Finish -> finish()
                else -> Unit
            }
        }
    }
}