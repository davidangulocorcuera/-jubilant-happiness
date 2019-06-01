package com.example.justfivemins.modules.showUsersScreen


import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_show_users.*


class ShowUsersFragment : BaseFragment(), ShowUsersPresenter.View {
    private val usersPresenter: ShowUsersPresenter by lazy { ShowUsersPresenter(this) }
    private lateinit var usersListAdapter: ShowUsersListAdapter
    private var users: ArrayList<User> = ArrayList()
    private val args: ShowUsersFragmentArgs by navArgs()


    override fun viewCreated(view: View?) {
        initUsersList()
        usersPresenter.init(args)
        btnBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
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
    override fun setUsers(usersLoaded: ArrayList<User>) {
        usersListAdapter.addAll(usersLoaded)
        usersListAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        initUsersList()
        usersPresenter.init(args)
    }
}
