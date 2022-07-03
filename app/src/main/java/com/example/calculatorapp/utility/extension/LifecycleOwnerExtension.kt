package com.example.calculatorapp.utility.extension

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

fun LifecycleOwner.delay(delay: Long, action: () -> Unit) {
    lifecycleScope.launch {
        kotlinx.coroutines.delay(delay)
        action.invoke()
    }
}

fun LifecycleOwner.throttleFirstClick(view: View, period: Long = 1500, action: (View) -> Unit) {
    view.throttleFirst(period).collect(lifecycleScope, action)
}
