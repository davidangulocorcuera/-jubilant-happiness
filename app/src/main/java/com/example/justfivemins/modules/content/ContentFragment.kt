package com.example.justfivemins.modules.content


import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.R.layout.fragment_content
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.base.Configurator
import com.example.justfivemins.modules.content.content_list_adapter.FeaturedUsersListAdapter
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.item_users_card_layout.*

class ContentFragment : BaseFragment() {
    private var users: ArrayList<User> = ArrayList()
    private lateinit var featuredUsersListAdapter: FeaturedUsersListAdapter


    override fun onCreateViewId(): Int {
        return fragment_content
    }

    override fun viewCreated(view: View?) {
        users.addAll(
            arrayOf(
                User(name = "Jesus Ortera", description = "18",profileImage = 1),
                User(name = "Jesus Ortera", description = "18",profileImage = 1),
                User(name = "Jesus Ortera", description = "18",profileImage = 1),
                User(name = "Jesus Ortera", description = "18",profileImage = 1),
                User(name = "Jesus Ortera", description = "18",profileImage = 1),
                User(name = "Jesus Ortera", description = "18",profileImage = 1),
                User(name = "Jesus Ortera", description = "18",profileImage = 1)
                )
        )
        val configurator: Configurator? = null
        setToolbarTitle(getString(R.string.home).toUpperCase())

        configurator?.hasToolbar = true
        initFeaturedList()

    }
    private fun initFeaturedList() {
        val layoutManager = LinearLayoutManager(activity,0,false)
        recyclerViewFeaturedUsersList.layoutManager = layoutManager as RecyclerView.LayoutManager?
        featuredUsersListAdapter =
            FeaturedUsersListAdapter(users)
        recyclerViewFeaturedUsersList.adapter = featuredUsersListAdapter
    }

    fun setButtonListener(){
        btnViewProfile.setOnClickListener {
        }
    }

}
