package com.example.justfivemins.modules.messages

import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class ChatPresenter(private val view: View) : MainMVP.Presenter {
    fun init(arguments: ChatFragmentArgs?) {
        arguments?.apply {
            if (this.user.id.isNotEmpty()) {
                view.setUser(user)
            } else {

            }
        }
    }

    interface View : MainMVP.View {
        fun setUser(user: User)
    }
}