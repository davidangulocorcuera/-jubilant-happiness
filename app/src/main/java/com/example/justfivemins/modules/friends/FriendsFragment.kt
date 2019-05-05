package com.example.justfivemins.modules.friends



import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseFragment


class FriendsFragment : BaseFragment() {
    override fun onCreateViewId(): Int {
        return R.layout.fragment_friends
    }

    override fun viewCreated(view: View?) {
        setToolbarTitle(getString(R.string.friends).toUpperCase())

    }



}
