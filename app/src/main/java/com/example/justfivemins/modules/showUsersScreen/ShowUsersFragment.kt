package com.example.justfivemins.modules.showUsersScreen

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_show_users.*


class ShowUsersFragment : BaseFragment(), ShowUsersPresenter.View {
    override fun setUsers(usersLoaded: ArrayList<User>) {
        usersListAdapter.addAll(usersLoaded)
        usersListAdapter.notifyDataSetChanged()
    }

    private val usersPresenter: ShowUsersPresenter by lazy { ShowUsersPresenter(this) }

    private lateinit var usersListAdapter: ShowUsersListAdapter
    private var users: ArrayList<User> = ArrayList()
    override fun viewCreated(view: View?) {
        initUsersList()
        usersPresenter.init(arguments)
    }

    override fun onCreateViewId(): Int {
        return R.layout.fragment_show_users
    }
    private fun initUsersList() {
        val layoutManager = GridLayoutManager(activity, 2)
        rvUsers.layoutManager = layoutManager as RecyclerView.LayoutManager?
        usersListAdapter =
            ShowUsersListAdapter(activity = activity!!)
        rvUsers.adapter = usersListAdapter
    }

}
