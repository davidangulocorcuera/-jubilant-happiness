package com.example.justfivemins


import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.justfivemins.modules.base.BaseActivity


class MainActivity : BaseActivity() {


    override fun onCreateViewId(): Int {
        return R.layout.activity_main
    }


    override fun goBack() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.navigateToLogin()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)


    }


}
