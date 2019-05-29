package com.example.justfivemins.modules.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class HomePresenter(private val view: View, val activity: Activity? = null) : MainMVP.Presenter, ApiEventsListeners.OnDataChangedListener{
    override fun isUserDataChanged(success: Boolean, userResponse: UserResponse) {
        if(success){
            view.getNewData(userResponse)
        }
    }

    private var currentUser: User = User()
    private var users = ArrayList<User>()
    private val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            onUserDataChangedListenerListener = this,
            activity = activity!!
        )
    }


    fun init(arguments: Intent?){
        if (arguments != null && users.isEmpty()) {
            users = arguments.getSerializableExtra("users") as ArrayList<User>
            view.setUsers(users)
            currentUser = arguments.getSerializableExtra("currentUser") as User
            view.setCurrentUser(currentUser)
        }
        firebaseApiManager.onUserDataChanged(CurrentUser.firebaseUser!!.uid)

    }

    interface View : MainMVP.View {
        fun showProgress(enable: Boolean)
        fun navigateToLocationFragment()
        fun loadHome()
        fun setUsers(users: ArrayList<User>)
        fun setCurrentUser(user: User)
        fun getNewData(userResponse: UserResponse)
    }
}