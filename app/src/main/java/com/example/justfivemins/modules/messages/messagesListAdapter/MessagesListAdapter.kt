package com.example.justfivemins.modules.messages.messagesListAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.justfivemins.R
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.chat.Message


import com.example.justfivemins.modules.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_user_cell.view.*


class MessagesListAdapter(val items: ArrayList<Message>, var context: Context) : BaseRecyclerAdapter<Message, MessagesListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_text_message, parent, false)

        return ViewHolder(v)
    }

    init {
        list = items
    }


    inner class ViewHolder(var view: View) : BaseRecyclerAdapter.ViewHolder(view) {
        private lateinit var current: Message


        override fun bind(position: Int) {
            current = getItem(position)
            setValues()

        }

        fun setValues() {


        }

    }
}