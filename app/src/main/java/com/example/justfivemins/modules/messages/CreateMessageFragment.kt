package com.example.justfivemins.modules.messages



import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseFragment


class CreateMessageFragment : BaseFragment() {
    override fun onCreateViewId(): Int {
        return R.layout.fragment_create_message
    }

    override fun viewCreated(view: View?) {
        setToolbarTitle(getString(R.string.messages).toUpperCase())

    }



}
