package com.example.justfivemins.modules.home.home_drawer

import com.example.justfivemins.R

class DrawerItem(var icon: Int = 0, var text: String? = "",
                 var isClickable: Boolean = false,
                 var isSeparator: Boolean = false, var type: DrawerViewModel.MenuItemType =  DrawerViewModel.MenuItemType.HOME) {

    companion object {
        fun addMenuOptions(menuOptions: ArrayList<DrawerItem>): ArrayList<DrawerItem> {
            menuOptions.add(DrawerItem(R.drawable.ic_home, "Home", isClickable = true, type = DrawerViewModel.MenuItemType.HOME))
            menuOptions.add(DrawerItem(R.drawable.ic_messages, "Messages",  isClickable = true,  type = DrawerViewModel.MenuItemType.MESSAGE))
            menuOptions.add(DrawerItem(R.drawable.ic_add_location, "Add Location", isClickable = true, type = DrawerViewModel.MenuItemType.MAP))
            menuOptions.add(DrawerItem(R.drawable.ic_friends, "Friends", isClickable = true, type = DrawerViewModel.MenuItemType.FRIENDS))
            menuOptions.add(DrawerItem(R.drawable.ic_images, "My photos", isClickable = true, type = DrawerViewModel.MenuItemType.MY_PICTURES))
            menuOptions.add(DrawerItem(R.drawable.ic_settings, "Personal Data", isClickable = true,type = DrawerViewModel.MenuItemType.PERSONAL_DATA))
            menuOptions.add(DrawerItem(R.drawable.ic_contact, "Contact us", isClickable = true, type = DrawerViewModel.MenuItemType.CONTACT))
            menuOptions.add(DrawerItem(R.drawable.ic_exit, "Close session", isClickable = true, type = DrawerViewModel.MenuItemType.LOG_OUT))
            return menuOptions

        }
    }
}