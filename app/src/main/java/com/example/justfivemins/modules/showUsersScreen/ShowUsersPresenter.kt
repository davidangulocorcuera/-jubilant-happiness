package com.example.justfivemins.modules.showUsersScreen

import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class ShowUsersPresenter(private val view: View) : MainMVP.Presenter {


    fun init(arguments: ShowUsersFragmentArgs?) {
        arguments?.apply {
            if (users.isNotEmpty()) {
                val usersFilteredArray = arguments.users
                val userFilteredList = usersFilteredArray.toList()
                val usersFilteredArrayList = ArrayList(userFilteredList)
                view.setUsers(usersFilteredArrayList)
            }
        }
    }
    interface View : MainMVP.View {
        fun setUsers(usersLoaded: ArrayList<User>)

    }
}


