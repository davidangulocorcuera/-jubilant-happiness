package com.example.justfivemins.modules.home

import android.util.Log
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class HomePresenter(private val view: View) : MainMVP.Presenter, ApiEventsListeners.UserDataListener {
    private val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(userDataListener = this) }
    private var user: User = User()

    override fun isUserDataSaved(success: Boolean, userResponse: UserResponse) {
        view.showProgress(false)
        if (success) {
            setUser(userResponse)


        } else {
            Log.v("taag", "fail getting firebaseUser data")
        }
    }

    fun setUser(userResponse: UserResponse) {
        user.name = userResponse.name
        user.email = userResponse.email
        user.birthday = userResponse.birthday
        user.currentLocation = userResponse.location
        view.setMenuData(user)
    }

    fun init() {
        view.showProgress(true)
        firebaseApiManager.getUserData(CurrentUser.firebaseUser!!)
    }

    interface View : MainMVP.View {
        fun showProgress(enable: Boolean)
        fun setMenuData(user: User)
        fun navigateToLocationFragment()
    }
}