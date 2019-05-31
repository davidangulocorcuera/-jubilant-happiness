package com.example.justfivemins.modules.location


import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.home.MainViewModel
import kotlinx.android.synthetic.main.fragment_location.*


class LocationFragment : BaseFragment(), ApiEventsListeners.LocationDataListener  {


    override fun onCreateViewId(): Int {
        return R.layout.fragment_location
    }
    override fun isLocationUpdated(success: Boolean) {
        if (success) {
            showProgress(false,false)
        } else {
            showProgress(false,false)

        }
    }

    override fun viewCreated(view: View?) {

        btnNext.setOnClickListener {
            showProgress(true,true)
            activity?.let { activity ->
                val mainViewModel = ViewModelProviders.of(activity).get(MainViewModel::class.java)
                    mainViewModel.getRandomLocation(activity.applicationContext)
                    mainViewModel.locationRequest.observe(this, Observer { locationRequest ->
                        locationRequest?.let {
                            mainViewModel.updateLocation(it, activity,this)
                        }
                    })
                    goToDownloadData()
            }
        }
    }
    private fun goToDownloadData() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.goToDownloadFragment)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        showProgress(false, false)
    }


}
