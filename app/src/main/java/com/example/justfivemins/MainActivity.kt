package com.example.justfivemins


import com.example.justfivemins.modules.base.BaseActivity


class MainActivity : BaseActivity() {
    override fun onCreateViewId(): Int {
        return R.layout.activity_main
    }

    override fun showProgress(show: Boolean, hasShade: Boolean) {
    }

    override fun showMessage(message: String) {
    }

    override fun showError(error: String) {
    }

    override fun goBack() {
    }




}
