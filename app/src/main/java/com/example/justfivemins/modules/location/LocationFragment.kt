package com.example.justfivemins.modules.location


import android.annotation.SuppressLint
import android.location.Geocoder
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.justfivemins.R
import com.example.justfivemins.api.requests.LocationRequest
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.home.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.fragment_location.*
import org.aviran.cookiebar2.CookieBar
import java.util.*


class LocationFragment : BaseFragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateViewId(): Int {
        return R.layout.fragment_location
    }


    override fun viewCreated(view: View?) {

        btnNext.setOnClickListener {

            val mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
            mainViewModel.getRandomLocation(activity?.applicationContext!!)
            mainViewModel.locationRequest.observe(this, Observer { locationRequest ->
                locationRequest?.let {
                    mainViewModel.updateLocation(it, activity!!)
                }
            })
            goToDownloadData()

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
