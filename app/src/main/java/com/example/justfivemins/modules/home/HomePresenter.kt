package com.example.justfivemins.modules.home

import android.util.Log
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.firebase.FirebaseListener
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class HomePresenter(private val view: View) : MainMVP.Presenter, FirebaseListener.UserDataListener {
    private val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(userDataListener = this) }
    private var user: User = User()

    override fun isUserDataOk(success: Boolean, userResponse: UserResponse) {
        view.showProgress(false)
        if (success) {
            setUser(userResponse)

        } else {
            Log.v("taag", "fail getting user data")
        }
    }

    fun setUser(userResponse: UserResponse) {
        user.name = userResponse.name
        user.email = userResponse.email
        user.surname = userResponse.surname
        view.setMenuData(user)
    }

    fun init() {
        view.showProgress(true)
        firebaseApiManager.getUserData(CurrentUser.user!!)
    }

    interface View : MainMVP.View {
        fun showProgress(enable: Boolean)
        fun setMenuData(user: User)
    }
}