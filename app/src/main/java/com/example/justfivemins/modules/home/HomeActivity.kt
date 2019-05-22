package com.example.justfivemins.modules.home

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.home.home_drawer.DrawerItem
import com.example.justfivemins.modules.home.home_drawer.DrawerListAdapter
import com.example.justfivemins.modules.home.home_drawer.DrawerViewModel
import com.example.justfivemins.modules.base.BaseActivity
import kotlinx.android.synthetic.main.drawer_menu.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_drawer_menu_header.view.*

class HomeActivity : BaseActivity(), HomePresenter.View {

    override fun showProgress(enable: Boolean) {
        showProgress(enable, enable)
    }

    private lateinit var toggleHome: ActionBarDrawerToggle
    private var menuOptions: ArrayList<DrawerItem> = ArrayList()
    private lateinit var drawerListAdapter: DrawerListAdapter
    private val presenter: HomePresenter by lazy {HomePresenter(this)}

    override fun onCreateViewId(): Int {
        return R.layout.drawer_menu
    }

   override fun setMenuData(user: User){
        menuNavigation.getHeaderView(0).tvMenuUsername.text = user.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.clearBackStack()
        presenter.init()
        navigator.navigateToContentFragment()
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
                    navigator.navigateToMessages()

                }
                DrawerViewModel.MenuItemType.MAP -> {
                    navigator.navigateToMap()
                }
                DrawerViewModel.MenuItemType.PERSONAL_DATA -> {
                    navigator.navigateToProfileData()
                }
                DrawerViewModel.MenuItemType.HOME -> {
                    navigator.navigateToContentFragment()
                }
                DrawerViewModel.MenuItemType.LOG_OUT -> {
                    navigator.navigateToMain()
                }
                DrawerViewModel.MenuItemType.FRIENDS -> {
                    navigator.navigateToFriends()
                }
                DrawerViewModel.MenuItemType.MY_PICTURES -> {
                    navigator.navigateToGallery()
                }
                DrawerViewModel.MenuItemType.CONTACT -> {
                }
            }
        }
    }


}
