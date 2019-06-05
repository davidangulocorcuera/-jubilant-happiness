package com.example.justfivemins.modules.showUsersScreen.userDetail

import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseDialogFragment

class UserDetailDialogFragment: BaseDialogFragment(),UsersDetailDialogPresenter.View {
    private val presenter: UsersDetailDialogPresenter by lazy { UsersDetailDialogPresenter(this) }

    override fun onCreateViewId(): Int {
       return R.layout.item_users_detail
    }

    override fun viewCreated(view: View?) {
    }
}