package com.example.justfivemins.modules.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.bumptech.glide.Glide
import com.example.justfivemins.api.responses.UserResponse
import kotlinx.android.synthetic.main.view_drawer_menu_header.*


class HomeActivity : BaseActivity(), HomePresenter.View , DrawerLocker {


    private val PICK_IMAGE_REQUEST = 1
    private lateinit var toggleHome: ActionBarDrawerToggle
    private var menuOptions: ArrayList<DrawerItem> = ArrayList()
    private lateinit var drawerListAdapter: DrawerListAdapter
    private val presenter: HomePresenter by lazy { HomePresenter(this,this) }
    private val users = ArrayList<User>()


    override fun onCreateViewId(): Int {
        return R.layout.drawer_menu
    }

    @SuppressLint("SetTextI18n")
    override fun setMenuData(user: User) {
        menuNavigation.getHeaderView(0).tvMenuUsername.text = user.name.capitalize()
        menuNavigation.getHeaderView(0).tvLocation.text = user.currentLocation?.city?.capitalize()

        if(user.profileImageUrl.isNotEmpty()){
            Glide
                .with(this)
                .load(user.profileImageUrl)
                .centerCrop()
                .into(ivDrawerProfileImage)

        }
        else{
            ivDrawerProfileImage.borderWidth = 8
            ivDrawerProfileImage.borderColor = getResourceColor(R.color.red)
            ivDrawerProfileImage.setImageResource(R.drawable.no_profile_image)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        showProgress(show = true, hasShade = true)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState)
        presenter.init()
        setDrawerMenu()
        menuOptions = DrawerItem.addMenuOptions(menuOptions)
        initList()
        menuNavigation.getHeaderView(0).ivDrawerProfileImage.setOnClickListener {
            navigator.navigateToProfileData()
        }
    }

    override fun onResume() {
        super.onResume()
        enableDrawerMenu(true)
        presenter.init()
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
                    navigator.addBackStack(true).navigateToProfileData()
                }
                DrawerViewModel.MenuItemType.HOME -> {
                    navigator.addExtra("users",users).navigateToFilterFragment()
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

    override fun loadHome() {
        navigator.navigateToFilterFragment()
        showProgress(false)

    }



    override fun navigateToLocationFragment() {
        navigator.addBackStack(false).navigateToRequestLocationDialog()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun setUsersList(response: ArrayList<UserResponse>) {
        var unknownUser: User = User()

        response.forEach {userResponse ->
            unknownUser.name = userResponse.name
            unknownUser.email = userResponse.email
            unknownUser.birthday = userResponse.birthday
            unknownUser.currentLocation = userResponse.location
            unknownUser.age = userResponse.age

            unknownUser.surname = userResponse.surname
            unknownUser.jobName = userResponse.job
            unknownUser.universityName = userResponse.university
            unknownUser.description = userResponse.description
            unknownUser.profileImageUrl = userResponse.profileImageUrl

            users.add(unknownUser)
            unknownUser = User()
        }

    }



}
