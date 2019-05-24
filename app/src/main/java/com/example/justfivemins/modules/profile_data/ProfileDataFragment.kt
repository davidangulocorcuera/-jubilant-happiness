package com.example.justfivemins.modules.profile_data

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import kotlinx.android.synthetic.main.view_profile_data.*

class ProfileDataFragment : BaseFragment() {
    private var user: User = User()


    override fun onCreateViewId(): Int {
        return R.layout.fragment_profile_data
    }

    override fun viewCreated(view: View?) {
        setPersonalData(user)
        setToolbarTitle(getString(R.string.my_data_title))

    }




    private fun setPersonalData(user: User) {

    }

}