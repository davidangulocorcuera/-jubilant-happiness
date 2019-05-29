package com.example.justfivemins.modules.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseActivity
import com.example.justfivemins.modules.home.home_drawer.DrawerItem
import com.example.justfivemins.modules.home.home_drawer.DrawerListAdapter
import com.example.justfivemins.modules.home.home_drawer.DrawerViewModel
import kotlinx.android.synthetic.main.drawer_menu.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_drawer_menu_header.view.*
import com.example.justfivemins.modules.home.home_drawer.DrawerLocker
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justfivemins.api.responses.UserResponse
import kotlinx.android.synthetic.main.view_drawer_menu_header.*


class HomeActivity : BaseActivity(), HomePresenter.View , DrawerLocker {


    private lateinit var toggleHome: ActionBarDrawerToggle
    private var menuOptions: ArrayList<DrawerItem> = ArrayList()
    private lateinit var drawerListAdapter: DrawerListAdapter
    private val presenter: HomePresenter by lazy { HomePresenter(this,this) }
    private var users = ArrayList<User>()
    private var currentUser = User()


    override fun onCreateViewId(): Int {
        showProgress(true)
        presenter.init(intent)
        return R.layout.drawer_menu
    }

    @SuppressLint("SetTextI18n")
     fun setMenuData(user: User) {
        menuNavigation.getHeaderView(0).tvMenuUsername.text = user.name.capitalize()
        menuNavigation.getHeaderView(0).tvLocation.text = user.currentLocation?.city?.capitalize()

        if(user.profileImageUrl.isNotEmpty()){
            Glide
                .with(this)
                .load(user.profileImageUrl)
                .centerCrop()
                .into(menuNavigation.getHeaderView(0).ivDrawerProfileImage)

        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_menu)
        loadHome()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setDrawerMenu()
        menuOptions = DrawerItem.addMenuOptions(menuOptions)
        initList()
        menuNavigation.getHeaderView(0).ivDrawerProfileImage.setOnClickListener {
//            navigator.navigateToProfileData()
        }
        setMenuData(currentUser)
    }

    override fun onResume() {
        super.onResume()
        enableDrawerMenu(true)
        loadHome()

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
//                    navigator.navigateToMessages()

                }
                DrawerViewModel.MenuItemType.MAP -> {
//                    navigator.navigateToMap()
                }
                DrawerViewModel.MenuItemType.PERSONAL_DATA -> {
                    loadHome()
                }
                DrawerViewModel.MenuItemType.HOME -> {
//                    navigator.addExtra("users",users).addExtra("currentUser",currentUser).navigateToFilterFragment()
                }
                DrawerViewModel.MenuItemType.LOG_OUT -> {
//                    navigator.navigateToMain()
                }
                DrawerViewModel.MenuItemType.FRIENDS -> {
//                    navigator.navigateToFriends()
                }
                DrawerViewModel.MenuItemType.MY_PICTURES -> {
//                    navigator.navigateToGallery()
                }
                DrawerViewModel.MenuItemType.CONTACT -> {
                }
            }
        }
    }

    override fun loadHome() {
//        navigator.addBackStack(false).addExtra("users",users).navigateToFilterFragment()
        showProgress(false)

    }



    override fun navigateToLocationFragment() {
//        navigator.addBackStack(false).navigateToRequestLocationDialog()
    }

    override fun showProgress(enable: Boolean) {
        showProgress(enable, enable)
    }
    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode = if (enabled)
            DrawerLayout.LOCK_MODE_UNLOCKED
        else
            DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawerLayout.setDrawerLockMode(lockMode)

        toggleHome.isDrawerIndicatorEnabled = enabled
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun setUsers(users: ArrayList<User>) {
        this.users = users
    }
    override fun setCurrentUser(user: User) {
        this.currentUser = user
    }
    override fun getNewData(userResponse: UserResponse){
        currentUser.name = userResponse.name
        currentUser.email = userResponse.email
        currentUser.birthday = userResponse.birthday
        currentUser.currentLocation = userResponse.location
        currentUser.age = userResponse.age

        currentUser.surname = userResponse.surname
        currentUser.jobName = userResponse.job
        currentUser.universityName = userResponse.university
        currentUser.description = userResponse.description
        currentUser.profileImageUrl = userResponse.profileImageUrl

    }





}
