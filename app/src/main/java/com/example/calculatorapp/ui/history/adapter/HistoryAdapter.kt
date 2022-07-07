package com.example.calculatorapp.ui.history.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.calculatorapp.databinding.ItemHistoryLayoutBinding
import com.example.calculatorapp.ui.adapter.BaseAdapter
import com.example.calculatorapp.ui.adapter.BaseViewHolder
import com.example.data.db.entity.History
import org.jetbrains.anko.layoutInflater

class HistoryAdapter(
    private val context: Context,
    lifecycleCoroutineScope: LifecycleCoroutineScope
) : BaseAdapter<ItemHistoryLayoutBinding, History>(lifecycleCoroutineScope) {

    override fun getBinding(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ItemHistoryLayoutBinding> = ViewHolder(ItemHistoryLayoutBinding.inflate(context.layoutInflater, parent, false))

    inner class ViewHolder(
        override val binding: ItemHistoryLayoutBinding
    ) : BaseViewHolder<ItemHistoryLayoutBinding>(binding) {
        override fun bind(position: Int) = with(binding) {
            val data = adapterList[position]

            expressionTextView.text = data.expression
            resultTextView.text = data.result
        }
    }
}