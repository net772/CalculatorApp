package com.example.calculatorapp.ui.history.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.calculatorapp.databinding.ItemHistoryLayoutBinding
import com.example.calculatorapp.ui.adapter.BaseAdapter
import com.example.calculatorapp.ui.adapter.BaseViewHolder
import com.example.data.db.entity.History

class HistoryAdapter(
    private val context: Context,
    lifecycleCoroutineScope: LifecycleCoroutineScope,
) : BaseAdapter<ItemHistoryLayoutBinding, History>(lifecycleCoroutineScope) {

    override fun getBinding(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ItemHistoryLayoutBinding> {
        val binding = ItemHistoryLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding).also { holder ->
            binding.btnRemove.setOnClickListener {
                if (RecyclerView.NO_POSITION != holder.adapterPosition) {
                    removeItem(holder.adapterPosition)
                }
            }
        }
    }

    fun removeItem(position: Int) {
        adapterList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(
        override val binding: ItemHistoryLayoutBinding,
    ) : BaseViewHolder<ItemHistoryLayoutBinding>(binding) {
        override fun bind(position: Int) = with(binding) {
            val data = adapterList[position]

            expressionTextView.text = data.expression
            resultTextView.text = data.result
        }
    }
}