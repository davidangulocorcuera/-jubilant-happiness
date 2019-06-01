package com.example.justfivemins.modules.home.homeFragment


import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.home.MainViewModel
import com.example.justfivemins.modules.home.home_drawer.DrawerItem
import com.example.justfivemins.modules.home.home_drawer.DrawerListAdapter
import com.example.justfivemins.modules.home.home_drawer.DrawerLocker
import com.example.justfivemins.modules.home.home_drawer.DrawerViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile_data.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_drawer_menu_header.view.*


class HomeFragment : BaseFragment(), DrawerLocker, ApiEventsListeners.LocationDataListener {

    override fun isLocationUpdated(success: Boolean) {
        menuNavigation.getHeaderView(0).tvLocation.isEnabled = true
        showProgress(false, false)
        if (success) {
        } else {

        }
    }


    private var currentUser = User()
    private lateinit var toggleHome: ActionBarDrawerToggle
    private var menuOptions: ArrayList<DrawerItem> = ArrayList()
    private lateinit var drawerListAdapter: DrawerListAdapter
    private var users: ArrayList<User> = ArrayList()
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel()::class.java)
    }


    override fun onCreateViewId(): Int {
        return R.layout.fragment_home
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.listenUserData()

    }

    override fun viewCreated(view: View?) {

        mainViewModel.response.observe(this, Observer { response ->
            response?.let {
                setNewData(it)
                showProgress(false)

            }
        })
        setDrawerMenu()
        menuOptions.clear()
        menuOptions = DrawerItem.addMenuOptions(menuOptions)
        initList()
        menuNavigation.getHeaderView(0).ivDrawerProfileImage.setOnClickListener {
            this.findNavController().navigate(R.id.goToProfileData)
        }
        setToolbarTitle(getString(R.string.home).toUpperCase())

        menuNavigation.getHeaderView(0).tvLocation.setOnClickListener {
            this.findNavController().navigate(R.id.goToMapFragment)
        }
        setMenuData()


    }

    private fun setDrawerMenu() {
        toggleHome = object : ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close) {
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
        val layoutManager = LinearLayoutManager(activity?.applicationContext)
        recyclerViewDrawerMenu.layoutManager = layoutManager as RecyclerView.LayoutManager?
        setMenuListener()
        recyclerViewDrawerMenu.adapter = drawerListAdapter
    }

    private fun setMenuListener() {
        drawerListAdapter = DrawerListAdapter(menuOptions) { menuItem ->
            when (menuItem.type) {
                DrawerViewModel.MenuItemType.MESSAGE -> {
                }
                DrawerViewModel.MenuItemType.MAP -> {
                    this.findNavController().navigate(R.id.goToMapFragment)
                }
                DrawerViewModel.MenuItemType.PERSONAL_DATA -> {
                    this.findNavController().navigate(R.id.goToProfileData)

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

    override fun onResume() {
        super.onResume()
        drawerLayout.closeDrawer(GravityCompat.START)
        mainViewModel.listenUserData()
    }

    @SuppressLint("SetTextI18n")
    fun setMenuData() {
        menuNavigation.getHeaderView(0).tvMenuUsername.text = CurrentUser.user?.name!!.capitalize()
        if (CurrentUser.user?.currentLocation?.city == "") {
            menuNavigation.getHeaderView(0).tvLocation.text = getString(R.string.add_address).capitalize()
        } else {
            menuNavigation.getHeaderView(0).tvLocation.text = CurrentUser.user?.currentLocation?.city?.capitalize()

        }
        if (CurrentUser.user?.profileImageUrl!!.isNotEmpty()) {
            Glide
                .with(this)
                .load(CurrentUser.user?.profileImageUrl)
                .centerCrop()
                .into(menuNavigation.getHeaderView(0).ivDrawerProfileImage)

        }
        else {
            menuNavigation.getHeaderView(0).ivDrawerProfileImage.setImageResource(R.drawable.no_profile_image)
        }
    }


    fun showProgress(enable: Boolean) {
        showProgress(enable, enable)
    }

    fun setCurrentUser(user: User) {
        this.currentUser = user
    }

    fun setNewData(userResponse: UserResponse) {
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
        CurrentUser.user = currentUser
        setMenuData()
    }

    fun setUsers(user: ArrayList<User>) {
        users = user
    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode = if (enabled)
            DrawerLayout.LOCK_MODE_UNLOCKED
        else
            DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawerLayout.setDrawerLockMode(lockMode)

        toggleHome.isDrawerIndicatorEnabled = enabled
    }

}








