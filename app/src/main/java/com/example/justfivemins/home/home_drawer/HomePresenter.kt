package com.example.justfivemins.home.home_drawer

import android.content.Intent
import com.example.justfivemins.modules.base.MainMVP

class HomePresenter(private val view: HomePresenter.View): MainMVP.Presenter {
    fun init(intent: Intent?) {


    }

    interface View : MainMVP.View {
        //FUNCTIONS
    }
}