package com.example.justfivemins.modules.register

import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseFragment

class RegisterFragment : BaseFragment() {
    override fun onCreateViewId(): Int {
        return R.layout.fragment_register
    }
    override fun viewCreated(view: View?) {
    }
    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }


}
