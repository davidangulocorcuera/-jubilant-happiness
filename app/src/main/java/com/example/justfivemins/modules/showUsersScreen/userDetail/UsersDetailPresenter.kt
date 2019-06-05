package com.example.justfivemins.modules.showUsersScreen.userDetail

import android.os.Bundle
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class UsersDetailPresenter(private val view: UsersDetailPresenter.View) : MainMVP.Presenter {
    private var user: User = User()

    fun init(arguments: Bundle?) {
        if (arguments != null && arguments["user"] != null) {
            user = arguments["user"] as User
            view.loadUser(user)
        }
    }

    interface View {
        fun loadUser(user: User)
    }
}