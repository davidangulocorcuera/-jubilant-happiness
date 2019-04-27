package com.example.justfivemins.modules.login


import android.os.Bundle
import com.example.justfivemins.modules.base.MainMVP

class LoginFragmentPresenter(private val view: View) : MainMVP.Presenter {

    val LOG_TAG = LoginFragmentPresenter::class.java.simpleName

    fun init(arguments: Bundle?) {

    }
    interface View : MainMVP.View {
        fun goToHome()
    }
}