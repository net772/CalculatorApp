package com.example.calculatorapp.ui.main

import androidx.fragment.app.Fragment
import com.example.calculatorapp.R
import com.example.calculatorapp.databinding.ActivityMainBinding
import com.example.calculatorapp.ui.base.BaseActivity
import com.example.calculatorapp.ui.calculator.CalculatorFragmentV2
import com.example.calculatorapp.ui.history.HistoryFragment
import com.example.calculatorapp.utility.extension.replaceFragment
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
                R.id.menu_calculator -> showFragment(CalculatorFragmentV2.newInstance())
                R.id.menu_history -> showFragment(HistoryFragment.newInstance())
            }
            true
        }

        bottomNav.selectedItemId = R.id.menu_calculator
    }

    private fun showFragment(fragment: Fragment) {
        replaceFragment(R.id.homeContainer, fragment)
    }
}