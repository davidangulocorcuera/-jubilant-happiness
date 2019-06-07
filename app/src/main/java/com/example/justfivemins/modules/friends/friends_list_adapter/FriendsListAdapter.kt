package com.example.justfivemins.modules.friends.friends_list_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justfivemins.R
import com.example.justfivemins.model.User


import com.example.justfivemins.modules.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_user_cell.view.*


class FriendsListAdapter(val items: ArrayList<User>) : BaseRecyclerAdapter<User, FriendsListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_cell, parent, false)

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

        fun setValues() {
            view.tvFriendUsername.text = current.name

        }

    }
}