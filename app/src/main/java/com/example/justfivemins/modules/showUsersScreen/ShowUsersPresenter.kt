package com.example.justfivemins.modules.showUsersScreen

import android.os.Bundle
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class ShowUsersPresenter (private val view: View): MainMVP.Presenter{
    private var users: ArrayList<User>? = null


    fun init(arguments: Bundle?){
        if (arguments != null) {
            users = arguments["users"] as ArrayList<User>
            view.setUsers(users!!)
        }
    }
    interface View : MainMVP.View {
        fun setUsers(usersLoaded: ArrayList<User>)
    }

}