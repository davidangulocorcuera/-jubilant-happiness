package com.example.justfivemins.modules.home

import android.util.Log
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.firebase.FirebaseListener
import com.example.justfivemins.modules.base.MainMVP
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User

class HomePresenter(private val view: View): MainMVP.Presenter, FirebaseListener.UserDataListener {
    private val firebaseApiManager = FirebaseApiManager()

    fun setUser(userResponse: UserResponse): User{
        val user: User = User()
        user.name = userResponse.name
        user.email = userResponse.email
        user.surname = userResponse.surname
        return user
    }

    fun init() {

    }
    override fun isUserDataOk(success: Boolean) {
        view.showProgress(false)
        if(success){
            setUser(firebaseApiManager.getUserData(CurrentUser.user!!))
        }
        else{
            Log.v("taag", "fail getting user data")
        }
    }

    interface View : MainMVP.View {
        fun showProgress(enable: Boolean)
        fun setUserData(user: User): User
        //FUNCTIONS
    }
}