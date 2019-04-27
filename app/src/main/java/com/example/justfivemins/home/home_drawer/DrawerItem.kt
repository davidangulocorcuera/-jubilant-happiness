package com.example.justfivemins.home.home_drawer

import com.example.justfivemins.R

class DrawerItem(var icon: Int = 0, var text: String? = "",
                 var notifications: Int = 0, var isClickable: Boolean = false,
                 var isSeparator: Boolean = false, var type: DrawerViewModel.MenuItemType =  DrawerViewModel.MenuItemType.HOME) {

    companion object {
        fun addMenuOptions(menuOptions: ArrayList<DrawerItem>): ArrayList<DrawerItem> {
            menuOptions.add(DrawerItem(R.drawable.ic_home, "Home", isClickable = true, isSeparator = true, type = DrawerViewModel.MenuItemType.HOME))
            menuOptions.add(DrawerItem(R.drawable.ic_settings, "Personal Data", isClickable = true, type = DrawerViewModel.MenuItemType.PERSONAL_DATA))
            menuOptions.add(DrawerItem(R.drawable.ic_add_location, "Add Location", isClickable = true,isSeparator = true, type = DrawerViewModel.MenuItemType.MAP))
            menuOptions.add(DrawerItem(R.drawable.ic_exit, "Close sesion", isClickable = true,isSeparator = true, type = DrawerViewModel.MenuItemType.LOG_OUT))

            return menuOptions

        }
        val messagesItem = DrawerItem(R.drawable.ic_launcher_background, "Mensajes", notifications = 2, isClickable = true, isSeparator = true, type = DrawerViewModel.MenuItemType.MESSAGE)
    }
}