package com.example.calculatorapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.calculatorapp.utility.extension.collect
import com.example.calculatorapp.utility.state.ResultUiState
import kotlinx.coroutines.flow.Flow

abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {

    private var _binding: Binding? = null
    protected val binding get() = _binding!!

    protected abstract fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): Binding

    protected open fun initFragment() = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = createFragmentBinding(inflater, container).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun <T> LiveData<T>.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }

    protected inline fun <T> Flow<T>.onResult(crossinline action: (T) -> Unit) {
        collect(viewLifecycleOwner.lifecycleScope, action)
    }

    protected inline fun <T> Flow<ResultUiState<T>>.onUiState(
        crossinline loading: () -> Unit = {},
        crossinline success: (T) -> Unit = {},
        crossinline error: (Throwable) -> Unit = {},
        crossinline finish: () -> Unit = {}
    ) {
        onResult { state ->
            when (state) {
                ResultUiState.Loading -> loading()
                is ResultUiState.Success -> success(state.data)
                is ResultUiState.Error -> error(state.error)
                ResultUiState.Finish -> finish()
                else -> Unit
            }
        }
    }

}