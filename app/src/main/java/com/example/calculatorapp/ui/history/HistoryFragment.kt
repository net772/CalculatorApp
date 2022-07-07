package com.example.calculatorapp.ui.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.calculatorapp.databinding.FragmentHistoryBinding
import com.example.calculatorapp.ui.base.BaseFragment
import com.example.calculatorapp.ui.history.adapter.HistoryAdapter
import com.example.calculatorapp.utility.extension.gone
import com.example.calculatorapp.utility.extension.throttleFirstClick
import com.example.calculatorapp.utility.extension.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    companion object {
        const val TAG = "HistoryFragment"
        fun newInstance() = HistoryFragment()
    }

    private val viewModel by viewModel<HistoryViewModel>()

    private val historyAdapter by lazy {
        HistoryAdapter(
            context = requireContext(),
            lifecycleCoroutineScope = lifecycleScope
        )
    }

    override fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHistoryBinding.inflate(inflater, container, false)

    override fun initFragment() {
        onClickEvent()
        initAdapter()
        observeViewModel()
        viewModel.getAllHistory()
    }

    private fun onClickEvent() = with(binding) {
        throttleFirstClick(historyClearButton) { clearHistoryList() }
    }

    private fun initAdapter() = with(binding) {
        historyRecyclerView.apply {
            adapter = historyAdapter
            hasFixedSize()
        }
    }

    private fun observeViewModel() {
        viewModel.historyData.onUiState(
            loading = { binding.progressBar.visible() },
            error = {
                Log.d("로그","에러")
            },
            success = { list ->
                historyAdapter.set(list)
            },
            finish = { binding.progressBar.gone() }
        )
    }

    private fun clearHistoryList() {
        viewModel.deleteAllHistory()
    }

}