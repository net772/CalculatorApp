package com.example.calculatorapp.ui.main

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.calculatorapp.utility.extension.replaceFragment
import com.example.calculatorapp.R
import com.example.calculatorapp.databinding.ActivityMainBinding
import com.example.calculatorapp.ui.base.BaseActivity
import com.example.calculatorapp.ui.calculator.CalculatorFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel by viewModel<MainViewModel>()

    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initActivity() {
        initView()
    }

    private fun initView() = with(binding) {
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_calculator -> showFragment(CalculatorFragment.newInstance(), CalculatorFragment.TAG)
               // R.id.menu_history -> showHistoryFragment()
            }
            true
        }

        bottomNav.selectedItemId = R.id.menu_calculator
    }

//    private fun showHistoryFragment() {
//        TODO("Not yet implemented")
//    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.homeContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }
}