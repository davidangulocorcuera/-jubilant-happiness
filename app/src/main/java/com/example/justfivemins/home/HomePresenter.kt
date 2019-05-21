package com.example.justfivemins.home

import android.content.Intent
import com.example.justfivemins.modules.base.MainMVP

class HomePresenter(private val view: View): MainMVP.Presenter {
    fun init(intent: Intent?) {

    }
    interface View : MainMVP.View {
        //FUNCTIONS
    }
}