package com.example.justfivemins.modules.friends



import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.friends.friends_list_adapter.FriendsListAdapter
import kotlinx.android.synthetic.main.fragment_friends.*

/**  Fragment for show all conversations that the user have open, need to be implemented */

class FriendsFragment : BaseFragment(),FriendsPresenter.View {
    private lateinit var friendsListAdapter: FriendsListAdapter
    private var users: ArrayList<User> = ArrayList()
    private val presenter: FriendsPresenter by lazy { FriendsPresenter(this) }



    override fun onCreateViewId(): Int {
        return R.layout.fragment_friends
    }

    override fun viewCreated(view: View?) {
        setToolbarTitle(getString(R.string.friends).toUpperCase())
        users.addAll(
            arrayOf(
                User()
            )
        )
        initUsersList()

    }

    private fun initUsersList() {
        val layoutManager = GridLayoutManager(activity, 1)
        rvFriends.layoutManager = layoutManager
        setListListener()
        rvFriends.adapter = friendsListAdapter
    }

    private fun setListListener() {
        friendsListAdapter =
                FriendsListAdapter(context = context!!,items = users) {
                    Log.v("taag", "users")

            }
    }


}
