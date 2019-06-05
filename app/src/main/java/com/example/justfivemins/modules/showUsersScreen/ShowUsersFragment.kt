package com.example.justfivemins.modules.showUsersScreen


import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.showUsersScreen.userDetail.UserDetailDialog
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
        rvUsers.layoutManager = layoutManager
        setListListener()
        rvUsers.adapter = usersListAdapter
    }

    private fun setListListener() {
        usersListAdapter =
            ShowUsersListAdapter(activity = activity!!) {
                showUserDialog(it)
            }
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

    private fun showUserDialog(user: User) {
        val dialogFragment = UserDetailDialog()
        val args = Bundle()
        args.putParcelable("user", user)
        dialogFragment.arguments = args
        activity?.supportFragmentManager?.let {
            dialogFragment.show(it, "user")
        }
    }
}
