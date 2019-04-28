package com.example.justfivemins.modules.register

import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment() {
    override fun onCreateViewId(): Int {
        return R.layout.fragment_register
    }

    override fun viewCreated(view: View?) {
        setToolbarTitle(getString(R.string.register))
        setListeners()

    }

    fun setListeners() {
        btnNext.setOnClickListener {
            goToNextScreen()
        }
        btnBack.setOnClickListener {
            backToLogin()
        }
    }

    fun goToNextScreen() {
        navigator.navigateToHome()

    }

    fun backToLogin() {
        navigator.navigateToLogin()
    }


}
