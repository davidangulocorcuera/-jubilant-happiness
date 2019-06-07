package com.example.justfivemins.modules.showUsersScreen

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.justfivemins.model.User


import com.example.justfivemins.modules.base.BaseRecyclerAdapter
import android.graphics.ColorMatrixColorFilter
import android.graphics.ColorMatrix
import com.example.justfivemins.R
import kotlinx.android.synthetic.main.item_user_card.view.*


class ShowUsersListAdapter(var items: ArrayList<User> = ArrayList(), val activity: Activity,var onUserClick: (User) -> Unit) : BaseRecyclerAdapter<User, ShowUsersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_card, parent, false)

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

            view.setOnClickListener {
                onUserClick(current)
            }
        }

        private fun setValues() {
            view.tvName.text = current.name
            view.tvAge.text = current.age.toString()
            view.tvLocation.text = current.currentLocation?.country


            if (current.profileImageUrl.isNotEmpty()) {
                Glide
                    .with(activity)
                    .load(current.profileImageUrl)
                    .centerCrop()
                    .into(view.ivProfileImage)

            } else {
                view.ivProfileImage.setImageResource(R.drawable.no_profile_image)
            }
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(matrix)
            view.ivProfileImage.setColorFilter(filter)


        }


    }
}