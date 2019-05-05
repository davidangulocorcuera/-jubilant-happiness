package com.example.justfivemins.modules.messages



import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseFragment


class MessagesFragment : BaseFragment() {
    override fun onCreateViewId(): Int {
        return R.layout.fragment_messages
    }

    override fun viewCreated(view: View?) {
        setToolbarTitle(getString(R.string.messages).toUpperCase())

    }



}
