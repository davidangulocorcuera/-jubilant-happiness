package com.example.justfivemins.modules.home.homeFragment


import android.annotation.SuppressLint
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.home.MainViewModel
import com.example.justfivemins.modules.home.home_drawer.DrawerItem
import com.example.justfivemins.modules.home.home_drawer.DrawerListAdapter
import com.example.justfivemins.modules.home.home_drawer.DrawerLocker
import com.example.justfivemins.modules.home.home_drawer.DrawerViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_drawer_menu_header.view.*
import android.widget.Toast









class HomeFragment : BaseFragment(), DrawerLocker {


    private var currentUser = User()
    private lateinit var toggleHome: ActionBarDrawerToggle
    private var menuOptions: ArrayList<DrawerItem> = ArrayList()
    private lateinit var drawerListAdapter: DrawerListAdapter
    private var users: ArrayList<User> = ArrayList()
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel()::class.java)
    }
    private lateinit var usersFilteredList: List<User>
    private lateinit var usersFilteredArray: Array<User>
    private lateinit var usersFilteredArrayList: ArrayList<User>


    override fun onCreateViewId(): Int {
        return R.layout.fragment_home
    }

    override fun viewCreated(view: View?) {
        setHasOptionsMenu(true)
        setObservers()
        setDrawerMenu()
        menuOptions.clear()
        context?.let {
            menuOptions = DrawerItem.addMenuOptions(menuOptions, it)
        }
        initList()
        menuNavigation.getHeaderView(0).ivDrawerProfileImage.setOnClickListener {
            this.findNavController().navigate(R.id.goToProfileData)
        }
        setToolbarTitle(getString(R.string.home).toUpperCase())

        menuNavigation.getHeaderView(0).tvLocation.setOnClickListener {
            this.findNavController().navigate(R.id.goToMapFragment)
        }
        setMenuData()
        setCardsOnClickListeners()

    }

    private fun setObservers() {
        mainViewModel.response.observe(this, Observer { response ->
            response?.let {
                setNewData(it)
                showProgress(false)

            }
        })
        mainViewModel.users.observe(this, Observer { response ->
            response?.let {
                users = it
                showProgress(false)
            }
        })
        mainViewModel.usersUpdatedResponse.observe(this, Observer { response ->
            response?.let {
                users = it
                Log.v("taag", users.size.toString())
                showProgress(false)
            }
        })
    }

    private fun setCardsOnClickListeners() {

        /**
         * Filter by country
         * */

        cvFilterByCountry.setOnClickListener {
            usersFilteredList = users.filter {
                it.currentLocation?.countryCode == CurrentUser.user?.currentLocation!!.countryCode
            }
            usersFilteredArrayList = ArrayList(usersFilteredList)
            usersFilteredArray = usersFilteredArrayList.toTypedArray()

            val action = HomeFragmentDirections.goToShowUsersFragment(usersFilteredArray)
            this.findNavController().navigate(action)
        }

        /**
         * Filter by city
         * */

        cvFilterByCity.setOnClickListener {
            usersFilteredList = users.filter {
                it.currentLocation?.city == CurrentUser.user?.currentLocation!!.city
            }
            usersFilteredArrayList = ArrayList(usersFilteredList)
            usersFilteredArray = usersFilteredArrayList.toTypedArray()

            val action = HomeFragmentDirections.goToShowUsersFragment(usersFilteredArray)
            this.findNavController().navigate(action)
        }

        /**
         * Filter by postal code
         * */

        cvFilterByPostalCode.setOnClickListener {
            usersFilteredList = users.filter {
                it.currentLocation?.postalCode == CurrentUser.user?.currentLocation!!.postalCode
            }
            usersFilteredArrayList = ArrayList(usersFilteredList)
            usersFilteredArray = usersFilteredArrayList.toTypedArray()

            val action = HomeFragmentDirections.goToShowUsersFragment(usersFilteredArray)
            this.findNavController().navigate(action)
        }

        /**
         * Filter by job
         * */

        cvFilterByJob.setOnClickListener {
            usersFilteredList = users.filter {
                it.jobName == CurrentUser.user?.jobName
            }
            usersFilteredArrayList = ArrayList(usersFilteredList)
            usersFilteredArray = usersFilteredArrayList.toTypedArray()

            val action = HomeFragmentDirections.goToShowUsersFragment(usersFilteredArray)
            this.findNavController().navigate(action)
        }

        /**
         * Filter by university
         * */

        cvFilterByUniversity.setOnClickListener {
            usersFilteredList = users.filter {
                it.universityName == CurrentUser.user?.universityName
            }
            usersFilteredArrayList = ArrayList(usersFilteredList)
            usersFilteredArray = usersFilteredArrayList.toTypedArray()

            val action = HomeFragmentDirections.goToShowUsersFragment(usersFilteredArray)
            this.findNavController().navigate(action)
        }

        /**
         * Filter by random
         * */

        cvRandomUsers.setOnClickListener {
            val action = HomeFragmentDirections.goToShowUsersFragment(users.toTypedArray())
            this.findNavController().navigate(action)
        }

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
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                DrawerViewModel.MenuItemType.LOG_OUT -> {
                    FirebaseAuth.getInstance().signOut()
                    this.findNavController().popBackStack()

                }
                DrawerViewModel.MenuItemType.FRIENDS -> {
                    this.findNavController().navigate(R.id.goToFriends)

                }
                DrawerViewModel.MenuItemType.MY_PICTURES -> {
                }
                DrawerViewModel.MenuItemType.CONTACT -> {
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    @SuppressLint("SetTextI18n")
    fun setMenuData() {
        menuNavigation.getHeaderView(0).tvMenuUsername.text = CurrentUser.user?.name!!.capitalize()
        if (CurrentUser.user?.currentLocation?.city == "") {
            menuNavigation.getHeaderView(0).tvLocation.text = getString(R.string.add_address).capitalize()
        } else {
            menuNavigation.getHeaderView(0).tvLocation.text =
                CurrentUser.user?.currentLocation?.country?.capitalize() + "," + CurrentUser.user?.currentLocation?.city?.capitalize()

        }
        if (CurrentUser.user?.profileImageUrl!!.isNotEmpty()) {
            Glide
                .with(this)
                .load(CurrentUser.user?.profileImageUrl)
                .centerCrop()
                .into(menuNavigation.getHeaderView(0).ivDrawerProfileImage)

        } else {
            menuNavigation.getHeaderView(0).ivDrawerProfileImage.setImageResource(R.drawable.no_profile_image)
        }
    }


    fun showProgress(enable: Boolean) {
        showProgress(enable, enable)
    }


    private fun setNewData(userResponse: UserResponse) {
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
        currentUser.id = userResponse.id

        CurrentUser.user = currentUser
        setMenuData()
    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode = if (enabled)
            DrawerLayout.LOCK_MODE_UNLOCKED
        else
            DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawerLayout.setDrawerLockMode(lockMode)

        toggleHome.isDrawerIndicatorEnabled = enabled
    }


    override fun onStart() {
        super.onStart()
        mainViewModel.listenUserData()
        mainViewModel.listenUsersData()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.navigation, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.english -> Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
            R.id.spanish -> Toast.makeText(context, "2", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}








