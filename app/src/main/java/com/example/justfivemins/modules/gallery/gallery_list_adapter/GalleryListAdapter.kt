package com.example.justfivemins.modules.gallery.gallery_list_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justfivemins.R


import com.example.justfivemins.model.CardItem
import com.example.justfivemins.modules.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.friends_cell_item.view.*


class GalleryListAdapter(val items: ArrayList<CardItem>) : BaseRecyclerAdapter<CardItem, GalleryListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_photo_card, parent, false)

        return ViewHolder(v)
    }

    init {
        list = items
    }


    inner class ViewHolder(var view: View) : BaseRecyclerAdapter.ViewHolder(view) {
        private lateinit var current: CardItem


        override fun bind(position: Int) {
            current = getItem(position)
            setValues()

        }

        fun setValues() {
            //view.tvFriendUsername.text = current.cardTitle

        }

    }
}