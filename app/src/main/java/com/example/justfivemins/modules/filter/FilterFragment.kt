package com.example.justfivemins.modules.filter


import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.R.layout.fragment_filter
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.base.Configurator
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.item_filter_people.view.*
import kotlinx.android.synthetic.main.item_users_card_layout.*

class FilterFragment : BaseFragment() {
    private var users: ArrayList<User> = ArrayList()


    override fun onCreateViewId(): Int {
        return fragment_filter
    }

    override fun viewCreated(view: View?) {
        filterByCountryContainer.tvFilterName.text = getString(R.string.country)
        filterByCountryContainer.ivBackgrounnd.setImageResource(R.drawable.country_image)
        filterByPostalCodeContainer.tvFilterName.text = getString(R.string.postal_code)
        filterByPostalCodeContainer.ivBackgrounnd.setImageResource(R.drawable.postal_code_image)
        randomContainer.tvFilterName.text = getString(R.string.random_users)
        randomContainer.ivBackgrounnd.setImageResource(R.drawable.random_image)
        filterByCityContainer.tvFilterName.text = getString(R.string.city)
        setToolbarTitle(getString(R.string.home).toUpperCase())
        configurator?.hasToolbar = true

    }

    fun setButtonListener(){
        btnViewProfile.setOnClickListener {
        }
    }

}
