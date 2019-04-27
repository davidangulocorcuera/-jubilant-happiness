package com.example.justfivemins.modules.profile_data

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import kotlinx.android.synthetic.main.view_profile_data.*

class ProfileDataFragment : BaseFragment() {
    private lateinit var profileDataListAdapter: ProfileDataListAdapter
    private var dataList: ArrayList<DataListItem> = ArrayList()
    private var user: User = User()


    override fun onCreateViewId(): Int {
        return R.layout.fragment_profile_data
    }

    override fun viewCreated(view: View?) {
        initList(rvUserData)
        setPersonalData(user)
        setToolbarTitle(getString(R.string.my_data_title))

    }

    private fun initList(recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        profileDataListAdapter = ProfileDataListAdapter(dataList)
        recyclerView.adapter = profileDataListAdapter
    }


    private fun setPersonalData(user: User) {
        dataList.clear()
        dataList.add(
            DataListItem(itemTitle = getString(R.string.name),
                itemSubtitle = user.name)
        )
        dataList.add(
            DataListItem(itemTitle = getString(R.string.surname),
                itemSubtitle = user.surname)
        )
        dataList.add(
            DataListItem(itemTitle = getString(R.string.second_surname),
                itemSubtitle = user.secondSurname)
        )
        dataList.add(
            DataListItem(itemTitle = getString(R.string.user_age),
                itemSubtitle = user.age.toString())
        )
        dataList.add(
            DataListItem(itemTitle = getString(R.string.gender),
                itemSubtitle = user.gender)
        )
        dataList.add(
            DataListItem(itemTitle = getString(R.string.phone_number),
                itemSubtitle = user.phoneNumber)
        )

    }
    companion object {
        fun newInstance(): ProfileDataFragment {
            return ProfileDataFragment()
        }
    }
}