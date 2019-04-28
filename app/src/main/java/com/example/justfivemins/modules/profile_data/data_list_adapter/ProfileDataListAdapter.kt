package com.example.justfivemins.modules.profile_data.data_list_adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseRecyclerAdapter
import com.example.justfivemins.modules.profile_data.DataListItem
import com.example.justfivemins.utils.setVisible
import kotlinx.android.synthetic.main.item_profile_data.view.*

class ProfileDataListAdapter(val items: ArrayList<DataListItem>) : BaseRecyclerAdapter<DataListItem, ProfileDataListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_profile_data, parent, false)

        return ViewHolder(v)
    }

    init {
        list = items
    }


    inner class ViewHolder(var view: View) : BaseRecyclerAdapter.ViewHolder(view) {
        private lateinit var current: DataListItem


        override fun bind(position: Int) {
            current = getItem(position)
            setValues()
            if (position == items.size - 1) {
                view.dataSeparator.setVisible(false)
            }
        }

        private fun setValues() {

            view.tvPersonalDataContent.text = current.itemTitle
            view.tvPersonalDataTitle.text = current.itemSubtitle
        }

    }
}
