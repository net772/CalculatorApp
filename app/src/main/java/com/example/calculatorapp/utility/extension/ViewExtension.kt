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