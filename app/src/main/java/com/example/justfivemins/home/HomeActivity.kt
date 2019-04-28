package com.example.justfivemins.home

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.home.home_drawer.DrawerItem
import com.example.justfivemins.home.home_drawer.DrawerListAdapter
import com.example.justfivemins.home.home_drawer.DrawerViewModel
import com.example.justfivemins.modules.base.BaseActivity
import kotlinx.android.synthetic.main.drawer_menu.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeActivity : BaseActivity() {


    private lateinit var toggleHome: ActionBarDrawerToggle
    private var menuOptions: ArrayList<DrawerItem> = ArrayList()
    private lateinit var drawerListAdapter: DrawerListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goToContentScreen()
        setDrawerMenu()
        menuOptions = DrawerItem.addMenuOptions(menuOptions)
        initList()
    }

    private fun setDrawerMenu() {
        toggleHome = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                toggleHome.syncState()
            }
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                toggleHome.syncState()
            }
        }
        drawerLayout.setDrawerListener(toggleHome)
        toggleHome.syncState()
    }

    private fun initList() {
        val layoutManager = LinearLayoutManager(this)
        recyclerViewDrawerMenu.layoutManager = layoutManager as RecyclerView.LayoutManager?
        setMenuListener()
        recyclerViewDrawerMenu.adapter = drawerListAdapter
    }

    private fun setMenuListener() {
        drawerListAdapter = DrawerListAdapter(menuOptions) { menuItem ->
            drawerLayout.closeDrawer(GravityCompat.START)
            when (menuItem.type) {
                DrawerViewModel.MenuItemType.MESSAGE -> {
                }
                DrawerViewModel.MenuItemType.MAP -> {
                    goToMapScreen()
                }
                DrawerViewModel.MenuItemType.PERSONAL_DATA -> {
                    goToProfileDataScreen()
                }
                DrawerViewModel.MenuItemType.HOME -> {
                    goToContentScreen()
                }
                DrawerViewModel.MenuItemType.LOG_OUT -> {
                    goToLoginScreen()
                }
            }
        }
    }


    override fun onCreateViewId(): Int {
        return  R.layout.drawer_menu
    }
    private fun goToProfileDataScreen() {
        navigator.navigateToProfileData()
    }
    private fun goToMapScreen() {
        navigator.navigateToMap()
    }
    private fun goToContentScreen(){
        navigator.navigateToContentFragment()
    }
    private fun goToLoginScreen(){
        navigator.navigateToMain()

    }


    override fun showProgress(show: Boolean, hasShade: Boolean) {
    }

    override fun showMessage(message: String) {
    }

    override fun showError(error: String) {
    }

    override fun goBack() {
    }
}
