package com.example.justfivemins.modules.home

import android.app.Activity
import android.util.Log
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class HomePresenter(private val view: View) : MainMVP.Presenter, ApiEventsListeners.UserDataListener {
    private var user: User = User()


    override fun isUserDataSaved(success: Boolean, userResponse: UserResponse) {
        view.showProgress(false)
        if (success) {
            setUser(userResponse)
            view.loadhome()

        } else {
            Log.v("taag", "fail getting firebaseUser data")
        }
    }

    fun setUser(userResponse: UserResponse) {
        user.name = userResponse.name
        user.email = userResponse.email
        user.birthday = userResponse.birthday
        user.currentLocation = userResponse.location
        user.age = userResponse.age

        user.surname = userResponse.surname
        user.jobName = userResponse.job
        user.universityName = userResponse.university
        user.description = userResponse.description

        view.setMenuData(user)
    }

    fun init(activity: Activity) {
        view.showProgress(true)
        val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(userDataListener = this, activity = activity) }
        firebaseApiManager.getUserData(CurrentUser.firebaseUser!!)
    }

    interface View : MainMVP.View {
        fun showProgress(enable: Boolean)
        fun setMenuData(user: User)
        fun navigateToLocationFragment()
        fun loadhome()
    }
}