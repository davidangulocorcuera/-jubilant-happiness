package com.example.justfivemins.modules.home.homeFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class HomeFragmentPresenter(private val view: View, val activity: Activity? = null): MainMVP.Presenter ,
    ApiEventsListeners.OnDataChangedListener {
    private var currentUser: User = User()
    private var users = ArrayList<User>()
    private val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            onUserDataChangedListenerListener = this,
            activity = activity!!
        )
    }
    override fun isUserDataChanged(success: Boolean, userResponse: UserResponse) {
        if(success){
            view.getNewData(userResponse)
        }
    }

    fun init(arguments: Bundle?){
        if (arguments != null && users.isEmpty()) {
            users = arguments.getSerializable("users") as ArrayList<User>
            view.setUsers(users)
            currentUser = arguments.getSerializable("currentUser") as User
            view.setCurrentUser(currentUser)
        }
        firebaseApiManager.onUserDataChanged(CurrentUser.firebaseUser!!.uid)

    }



    interface View : MainMVP.View {

        fun showProgress(enable: Boolean)
        fun setUsers(users: ArrayList<User>)
        fun setCurrentUser(user: User)
        fun getNewData(userResponse: UserResponse)
    }

}