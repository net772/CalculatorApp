package com.example.calculatorapp.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffUtilAdapter<B : ViewDataBinding, T>(
    lifecycleScope: LifecycleCoroutineScope
) : BaseAdapter<B, T>(lifecycleScope) {

    protected abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    protected abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    private val itemCallback by lazy {
        object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
                this@BaseDiffUtilAdapter.areItemsTheSame(oldItem, newItem)

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
                this@BaseDiffUtilAdapter.areContentsTheSame(oldItem, newItem)
        }
    }

    private val asyncAdapterList = AsyncListDiffer(this, itemCallback)

    override val adapterList: MutableList<T>
        get() = asyncAdapterList.currentList

    open fun submit(list: List<T>, complete: () -> Unit = {}) {
        asyncAdapterList.submitList(list, complete)
    }

}