package com.example.calculatorapp.utility.extension

import android.view.View
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun View.throttleFirst(periodMillis: Long): Flow<View> = callbackFlow {

    var lastTime = 0L

    this@throttleFirst.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime >= periodMillis) {
            lastTime = currentTime
            this.offer(this@throttleFirst)
        }
    }
    awaitClose { this@throttleFirst.setOnClickListener(null) }
}

fun View.visible(visible: Boolean = true): Boolean {
    visibility = if (visible) View.VISIBLE else View.GONE
    return visible
}

fun View.invisible(invisible: Boolean = true): Boolean {
    visibility = if (invisible) View.INVISIBLE else View.VISIBLE
    return invisible
}

fun View.gone(gone: Boolean = true): Boolean {
    visibility = if (gone) View.GONE else View.VISIBLE
    return gone
}
