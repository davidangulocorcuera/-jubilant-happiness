package com.example.justfivemins.modules.home


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseActivity
import android.provider.MediaStore
import android.util.Log
import android.view.View
import java.io.File
import java.io.IOException
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.home.home_drawer.DrawerItem
import com.example.justfivemins.modules.home.home_drawer.DrawerListAdapter
import com.example.justfivemins.modules.home.home_drawer.DrawerLocker
import com.example.justfivemins.modules.home.home_drawer.DrawerViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.menuNavigation
import kotlinx.android.synthetic.main.fragment_home.recyclerViewDrawerMenu
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_drawer_menu_header.view.*


class MainActivity : BaseActivity(), DrawerLocker {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel()::class.java)
    }
    private lateinit var navController: NavController
    override fun onCreateViewId(): Int {
        return R.layout.activity_main
    }
    private lateinit var toggleHome: ActionBarDrawerToggle
    private var menuOptions: ArrayList<DrawerItem> = ArrayList()
    private lateinit var drawerListAdapter: DrawerListAdapter
    private var currentUser = User()
    private var users: ArrayList<User> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        navController = findNavController(R.id.navHostActivity)
        setObservers()
        setDrawerMenu()
        menuOptions.clear()
        menuOptions = DrawerItem.addMenuOptions(menuOptions, applicationContext)
        initList()
        menuNavigation.getHeaderView(0).ivDrawerProfileImage.setOnClickListener {
            navController.navigate(R.id.goToProfileData)
        }
        menuNavigation.getHeaderView(0).tvLocation.setOnClickListener {
           navController.navigate(R.id.goToMapFragment)
        }
        setMenuData()

    }


    override fun onResume() {
        super.onResume()
        showProgress(false, false)
    }

    /** it catch the image and send it to the view model */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showProgress(show = true, hasShade = true)
        data?.data?.let {
            mainViewModel.profileImageBitmap.postValue(adjustProfilePicture(it))
        }
    }


    /** temporal function for rotate the image */
    private fun adjustProfilePicture(selectedPicture: Uri): Bitmap {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this.contentResolver?.query(selectedPicture, filePathColumn, null, null, null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath = cursor?.getString(columnIndex!!)
        cursor?.close()

        var loadedBitmap = BitmapFactory.decodeFile(picturePath)

        var exif: ExifInterface? = null
        try {
            val pictureFile = File(picturePath)
            exif = ExifInterface(pictureFile.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var orientation = ExifInterface.ORIENTATION_NORMAL

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                loadedBitmap = rotateBitmap(loadedBitmap, 90)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                loadedBitmap = rotateBitmap(loadedBitmap, 180)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                loadedBitmap = rotateBitmap(loadedBitmap, 270)
            }
        }
        return loadedBitmap

    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
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

    private fun setObservers() {
        mainViewModel.response.observe(this, Observer { response ->
            response?.let {
                setNewData(it)
                showProgress(show = false, hasShade = false)
            }
        })
    }


    @SuppressLint("SetTextI18n")
    fun setMenuData() {
        CurrentUser.user?.let {
            menuNavigation.getHeaderView(0).tvMenuUsername.text = it.name.capitalize()
            if (CurrentUser.user?.currentLocation?.city == "") {
                menuNavigation.getHeaderView(0).tvLocation.text = getString(R.string.add_address).capitalize()
            } else {
                menuNavigation.getHeaderView(0).tvLocation.text =
                    CurrentUser.user?.currentLocation?.country?.capitalize() + "," + it.currentLocation?.city?.capitalize()

            }
            if (it.profileImageUrl.isNotEmpty()) {
                Glide
                    .with(this)
                    .load(CurrentUser.user?.profileImageUrl)
                    .centerCrop()
                    .into(menuNavigation.getHeaderView(0).ivDrawerProfileImage)

            } else {
                menuNavigation.getHeaderView(0).ivDrawerProfileImage.setImageResource(R.drawable.no_profile_image)
            }
        }


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
        val layoutManager = LinearLayoutManager(applicationContext)
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
                    navController.navigate(R.id.goToMapFragment)
                }
                DrawerViewModel.MenuItemType.PERSONAL_DATA -> {
                    navController.navigate(R.id.goToProfileData)
                }
                DrawerViewModel.MenuItemType.HOME -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                DrawerViewModel.MenuItemType.LOG_OUT -> {
                    FirebaseAuth.getInstance().signOut()
                    navController.popBackStack()

                }
                DrawerViewModel.MenuItemType.FRIENDS -> {
                    navController.navigate(R.id.goToFriends)

                }
                DrawerViewModel.MenuItemType.MY_PICTURES -> {
                }
                DrawerViewModel.MenuItemType.CONTACT -> {
                }

            }
        }
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
