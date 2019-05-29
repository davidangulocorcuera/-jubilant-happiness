package com.example.justfivemins.modules.filter


import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.item_filter_people.view.*


class FilterFragment : BaseFragment(), FilterPresenter.View {

    private var users: ArrayList<User> = ArrayList()
    private val filterPresenter: FilterPresenter by lazy { FilterPresenter(this) }


    override fun onCreateViewId(): Int {
        filterPresenter.init(arguments)
        return R.layout.fragment_filter
    }

    override fun setUsers(user: ArrayList<User>) {
        users = user
    }


    override fun viewCreated(view: View?) {
        showProgress(true, true)
        filterByCountryContainer.tvFilterName.text = getString(R.string.country)
        filterByCountryContainer.ivBackgrounnd.setImageResource(R.drawable.country_image)
        filterByPostalCodeContainer.tvFilterName.text = getString(R.string.postal_code)
        filterByPostalCodeContainer.ivBackgrounnd.setImageResource(R.drawable.postal_code_image)
        randomContainer.tvFilterName.text = getString(R.string.random_users)
        randomContainer.ivBackgrounnd.setImageResource(R.drawable.random_image)
        filterByCityContainer.tvFilterName.text = getString(R.string.city)
        setToolbarTitle(getString(R.string.home).toUpperCase())
        configurator?.hasToolbar = true
        showProgress(false, false)


        filterByCountryContainer.setOnClickListener {
            navigator.addExtra("users", users)
                .navigateToShowUsers()
        }
    }

}








