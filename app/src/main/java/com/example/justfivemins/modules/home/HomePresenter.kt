package com.example.justfivemins.modules.home

import com.example.justfivemins.firebase.FirebaseApiManager
import com.example.justfivemins.modules.base.MainMVP
import com.example.justfivemins.modules.responses.UserResponse

class HomePresenter(private val view: View): MainMVP.Presenter {
    val firebaseApiManager = FirebaseApiManager()
    private lateinit var userResponse: UserResponse

    fun init() {
       userResponse =  firebaseApiManager.getUserData(firebaseApiManager.auth.currentUser!!)
    }

    interface View : MainMVP.View {
        //FUNCTIONS
    }
}