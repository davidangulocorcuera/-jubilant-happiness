package com.example.justfivemins.modules.home.homeFragment


import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.home.home_drawer.DrawerItem
import com.example.justfivemins.modules.home.home_drawer.DrawerListAdapter
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.item_filter_people.view.*


class HomeFragment : BaseFragment(), HomeFragmentPresenter.View {
    private var menuOptions: ArrayList<DrawerItem> = ArrayList()
    private lateinit var drawerListAdapter: DrawerListAdapter
    private var users: ArrayList<User> = ArrayList()
    private val homeFragmentPresenter: HomeFragmentPresenter by lazy { HomeFragmentPresenter(this) }


    override fun onCreateViewId(): Int {
        homeFragmentPresenter.init(arguments)
        return R.layout.fragment_filter
    }

    override fun setUsers(user: ArrayList<User>) {
        users = user
    }

    override fun viewCreated(view: View?) {
        showProgress(true, true)
        filterByCountryContainer.tvFilterName.text = getString(R.string.country)
        filterByPostalCodeContainer.tvFilterName.text = getString(R.string.postal_code)
        randomContainer.tvFilterName.text = getString(R.string.random_users)
        filterByCityContainer.tvFilterName.text = getString(R.string.city)
        setToolbarTitle(getString(R.string.home).toUpperCase())
        configurator?.hasToolbar = true
        showProgress(false, false)


        filterByCountryContainer.setOnClickListener {
//            navigator.addExtra("users", users)
//                .navigateToShowUsers()
        }
    }

}








