package com.example.justfivemins.modules.content


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.R.layout.fragment_content
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.base.Configurator
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.item_filter_people.view.*
import kotlinx.android.synthetic.main.item_users_card_layout.*

class ContentFragment : BaseFragment() {
    private var users: ArrayList<User> = ArrayList()


    override fun onCreateViewId(): Int {
        return fragment_content
    }

    override fun viewCreated(view: View?) {
        filterByCountryContainer.tvFilterName.text = getString(R.string.country)
        filterByCountryContainer.ivBackgrounnd.setImageResource(R.drawable.country_image)
        filterByPostalCodeContainer.tvFilterName.text = getString(R.string.postal_code)
        filterByPostalCodeContainer.ivBackgrounnd.setImageResource(R.drawable.postal_code_image)
        randomContainer.tvFilterName.text = getString(R.string.random_users)
        randomContainer.ivBackgrounnd.setImageResource(R.drawable.random_image)
        filterByCityContainer.tvFilterName.text = getString(R.string.city)

        val configurator: Configurator? = null
        setToolbarTitle(getString(R.string.home).toUpperCase())

        configurator?.hasToolbar = true

    }

    fun setButtonListener(){
        btnViewProfile.setOnClickListener {
        }
    }

}
