package com.example.justfivemins.modules.home.home_drawer


/**  Interface for close menu from other fragments, the menu need to be changed from HomeFragment to MainActivity*/

interface DrawerLocker {
    fun setDrawerEnabled(enabled: Boolean)
}