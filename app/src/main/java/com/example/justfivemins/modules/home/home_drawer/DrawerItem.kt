package com.example.justfivemins.modules.home.home_drawer

import android.content.Context
import com.example.justfivemins.R

class DrawerItem(var icon: Int = 0, var text: String? = "",
                 var isClickable: Boolean = false,
                 var isSeparator: Boolean = false, var type: DrawerViewModel.MenuItemType =  DrawerViewModel.MenuItemType.HOME) {

    companion object {
        fun addMenuOptions(menuOptions: ArrayList<DrawerItem>,context: Context): ArrayList<DrawerItem> {
            menuOptions.add(DrawerItem(R.drawable.ic_home,  context.getString(R.string.home), isClickable = true, type = DrawerViewModel.MenuItemType.HOME))
            menuOptions.add(DrawerItem(R.drawable.ic_messages,  context.getString(R.string.messages),  isClickable = true,  type = DrawerViewModel.MenuItemType.MESSAGE))
            menuOptions.add(DrawerItem(R.drawable.ic_add_location,  context.getString(R.string.add_address), isClickable = true, type = DrawerViewModel.MenuItemType.MAP))
            menuOptions.add(DrawerItem(R.drawable.ic_friends, context.getString(R.string.friends), isClickable = true, type = DrawerViewModel.MenuItemType.FRIENDS))
            menuOptions.add(DrawerItem(R.drawable.ic_images, "My photos", isClickable = true, type = DrawerViewModel.MenuItemType.MY_PICTURES))
            menuOptions.add(DrawerItem(R.drawable.ic_settings,  context.getString(R.string.personal_data), isClickable = true,type = DrawerViewModel.MenuItemType.PERSONAL_DATA))
            menuOptions.add(DrawerItem(R.drawable.ic_contact, "Contact us", isClickable = true, type = DrawerViewModel.MenuItemType.CONTACT))
            menuOptions.add(DrawerItem(R.drawable.ic_exit,  context.getString(R.string.exit), isClickable = true, type = DrawerViewModel.MenuItemType.LOG_OUT))
            menuOptions.add(DrawerItem(R.drawable.ic_remove_forever,  context.getString(R.string.delete_account), isClickable = true, type = DrawerViewModel.MenuItemType.DELETE_ACCOUNT))
            return menuOptions

        }
    }
}