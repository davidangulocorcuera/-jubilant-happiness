package com.example.justfivemins.modules.friends


import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.model.CardItem
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.friends.friends_list_adapter.FriendsListAdapter
import kotlinx.android.synthetic.main.fragment_friends.*


class FriendsFragment : BaseFragment() {
    private lateinit var friendsListAdapter: FriendsListAdapter
    private var cards: ArrayList<CardItem> = ArrayList()


    override fun onCreateViewId(): Int {
        return R.layout.fragment_friends
    }

    override fun viewCreated(view: View?) {
        setToolbarTitle(getString(R.string.friends).toUpperCase())
        cards.addAll(
            arrayOf(
                User(cardTitle = "Jesus Ortera", cardDescription = "18", cardImage = 1),
                User(cardTitle = "Carlos Cebollo", cardDescription = "38", cardImage = 1),
                User(cardTitle = "Jorge Persiana", cardDescription = "19", cardImage = 1),
                User(cardTitle = "Maria Verdura", cardDescription = "22", cardImage = 1),
                User(cardTitle = "Jesus Ortera", cardDescription = "18", cardImage = 1),
                User(cardTitle = "Carlos Cebollo", cardDescription = "38", cardImage = 1),
                User(cardTitle = "Jorge Persiana", cardDescription = "19", cardImage = 1),
                User(cardTitle = "Maria Verdura", cardDescription = "22", cardImage = 1),
                User(cardTitle = "Carlos Cebollo", cardDescription = "38", cardImage = 1),
                User(cardTitle = "Jorge Persiana", cardDescription = "19", cardImage = 1),
                User(cardTitle = "Maria Verdura", cardDescription = "22", cardImage = 1),
                User(cardTitle = "Jesus Ortera", cardDescription = "18", cardImage = 1),
                User(cardTitle = "Carlos Cebollo", cardDescription = "38", cardImage = 1),
                User(cardTitle = "Jorge Persiana", cardDescription = "19", cardImage = 1),
                User(cardTitle = "Maria Verdura", cardDescription = "22", cardImage = 1)
            )
        )
        initUsersList()

    }

    private fun initUsersList() {
        val layoutManager = GridLayoutManager(activity, 1)
        rvFriends.layoutManager = layoutManager as RecyclerView.LayoutManager?
        friendsListAdapter =
            FriendsListAdapter(cards)
        rvFriends.adapter = friendsListAdapter
    }


}
