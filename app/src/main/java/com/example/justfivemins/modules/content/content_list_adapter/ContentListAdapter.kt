package com.example.justfivemins.modules.content.content_list_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justfivemins.R


import com.example.justfivemins.model.CellItem
import com.example.justfivemins.modules.base.BaseRecyclerAdapter
import com.example.justfivemins.utils.setVisible
import kotlinx.android.synthetic.main.item_content_card_layout.view.*


class ContentListAdapter(val items: ArrayList<CellItem>) : BaseRecyclerAdapter<CellItem, ContentListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_content_card_layout, parent, false)

        return ViewHolder(v)
    }

    init {
        list = items
    }


    inner class ViewHolder(var view: View) : BaseRecyclerAdapter.ViewHolder(view) {
        private lateinit var current: CellItem


        override fun bind(position: Int) {
            current = getItem(position)
            setValues()

        }

        fun setValues() {
            view.textViewName.text = current.title
            view.textViewDescription.text = current.description
            view.imageView_cellImage.setImageResource(R.drawable.img_profile_example)
            view.textViewPlace.text = "madrid"


        }

    }
}