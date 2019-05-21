package com.example.justfivemins.modules.home.home_drawer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseRecyclerAdapter
import com.example.justfivemins.utils.setVisible
import com.example.justfivemins.utils.tint
import kotlinx.android.synthetic.main.item_drawer_menu.view.*


class DrawerListAdapter(val items: ArrayList<DrawerItem>, var onMenuItemClick: (DrawerItem) -> Unit) :
    BaseRecyclerAdapter<DrawerItem, DrawerListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_drawer_menu, parent, false)
        return ViewHolder(v)
    }

    init {
        list = items
    }


    inner class ViewHolder(var view: View) : BaseRecyclerAdapter.ViewHolder(view) {
        private lateinit var current: DrawerItem


        override fun bind(position: Int) {
            current = getItem(position)
            setColors(position)
            hideAndShowElements()
            setValues()

            itemView.setOnClickListener {
                onMenuItemClick(current)
            }
        }


        private fun setColors(position: Int) {
            current = getItem(position)
            if (!current.isClickable) {
                view.menuItemContainer.setBackgroundResource(R.color.transparent)

            }
            when (current.type){
                DrawerViewModel.MenuItemType.HOME -> {view.ivDrawerIcon.tint(R.color.colorAccent)}
                DrawerViewModel.MenuItemType.PERSONAL_DATA -> {view.ivDrawerIcon.tint(R.color.colorPrimary)}
                DrawerViewModel.MenuItemType.MAP -> { view.ivDrawerIcon.tint(R.color.colorPrimary)}
                DrawerViewModel.MenuItemType.MESSAGE -> {  view.ivDrawerIcon.tint(R.color.colorPrimary)}
                DrawerViewModel.MenuItemType.LOG_OUT -> { view.ivDrawerIcon.tint(R.color.colorPrimaryDark)}
                DrawerViewModel.MenuItemType.FRIENDS -> { view.ivDrawerIcon.tint(R.color.colorPrimary)}
                DrawerViewModel.MenuItemType.MY_PICTURES -> { view.ivDrawerIcon.tint(R.color.colorPrimary)}
                DrawerViewModel.MenuItemType.CONTACT -> {view.ivDrawerIcon.tint(R.color.colorPrimary)}
            }

        }

        private fun hideAndShowElements() {
            view.drawerSeparator.setVisible(current.isSeparator)
        }

        private fun setValues() {
            // view.isClickable = current.isContent

            view.ivDrawerIcon.setImageResource(current.icon)
            view.tvText.text = current.text

        }

    }
}