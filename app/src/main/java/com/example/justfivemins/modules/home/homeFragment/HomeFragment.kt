package com.example.justfivemins.modules.home.homeFragment


import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.justfivemins.R
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.home.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(){


    private var currentUser = User()
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
        setCardsOnClickListeners()
        enableDrawerMenu(true)
        setObservers()

    }
    private fun setObservers() {
        mainViewModel.users.observe(this, Observer { usersResponse ->
            usersResponse?.let {
                users = it
                showProgress(show = false, hasShade = false)
            }
        })
        mainViewModel.usersUpdatedResponse.observe(this, Observer { usersResponse ->
            usersResponse?.let {
                users = it
                Log.v("taag", users.size.toString())
                showProgress(show = false, hasShade = false)
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
        //setMenuData()
    }


    override fun onResume() {
        super.onResume()
        showProgress(false,false)
    }




    override fun onStart() {
        super.onStart()
        mainViewModel.listenUserData()
        mainViewModel.listenUsersData()
    }

}








