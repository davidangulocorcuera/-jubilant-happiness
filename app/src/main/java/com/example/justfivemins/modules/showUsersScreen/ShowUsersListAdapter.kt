package com.example.justfivemins.modules.showUsersScreen

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.justfivemins.R
import com.example.justfivemins.model.User


import com.example.justfivemins.modules.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.friends_cell_item.view.*
import kotlinx.android.synthetic.main.item_users_card_layout.view.*


class ShowUsersListAdapter(val items: ArrayList<User>, val activity: Activity) : BaseRecyclerAdapter<User, ShowUsersListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_users_card_layout, parent, false)

        return ViewHolder(v)
    }

    init {
        list = items
    }


    inner class ViewHolder(var view: View) : BaseRecyclerAdapter.ViewHolder(view) {
        private lateinit var current: User


        override fun bind(position: Int) {
            current = getItem(position)
            setValues()

        }

        private fun setValues() {
            view.tvName.text = current.name
            view.tvAge.text = current.age.toString()
            Glide
                .with(activity)
                .load(current.profileImageUrl)
                .centerCrop()
                .into(view.ivProfileImage)

        }

    }
}