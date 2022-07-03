package com.example.calculatorapp.utility.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.calculatorapp.utility.config.ActivityConstants
import com.example.calculatorapp.R

fun FragmentActivity.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment?,
    action: String = "",
    addBackStack: Boolean = false
) {
    requireNotNull(fragment)

    val transaction = supportFragmentManager.beginTransaction()
    animFragment(transaction, action)
    transaction.replace(containerId, fragment).apply {
        if (addBackStack) addToBackStack(null)
    }
    transaction.commitAllowingStateLoss()
}

private fun animFragment(transaction: FragmentTransaction, action: String) {
    when (action) {
        ActivityConstants.LEFT -> {
            transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out)
        }
        ActivityConstants.RIGHT -> {
            transaction.setCustomAnimations(R.anim.right_in, R.anim.right_out)
        }
        ActivityConstants.UP -> {
            transaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out)
        }
        ActivityConstants.DOWN -> {
            transaction.setCustomAnimations(R.anim.push_down_in, R.anim.push_down_out)
        }
    }
}