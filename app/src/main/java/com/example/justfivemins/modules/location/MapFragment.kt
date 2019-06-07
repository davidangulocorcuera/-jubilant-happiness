package com.example.justfivemins.modules.location


import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.home.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker







class MapFragment : BaseFragment(), OnMapReadyCallback, ApiEventsListeners.LocationDataListener  {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel()::class.java)
    }
    private lateinit var mMap: GoogleMap

    override fun onCreateViewId(): Int {
        return R.layout.fragment_map
    }

    override fun viewCreated(view: View?) {
        btnBack.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.goToHomeFragment)

        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val currentLatLon = LatLng(CurrentUser.user?.currentLocation!!.lat, CurrentUser.user?.currentLocation!!.lng)
        mMap.addMarker(MarkerOptions().position(currentLatLon))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLon, 0.5f))
        mMap.setOnMapLongClickListener { latLon ->

            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLon))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLon, 0.5f))

            btnNext.setOnClickListener {
                showProgress(true,true)

                view?.let {
                    activity?.let { activity ->
                        mainViewModel.getAddressFromCoordinates(latLon.latitude, latLon.longitude ,activity.applicationContext)
                        mainViewModel.locationRequest.observe(this, Observer { locationRequest ->
                            locationRequest?.let {
                                mainViewModel.updateLocation(it, activity,this)
                            }
                        })
                    }
                }

            }
        }

    }
    override fun isLocationUpdated(success: Boolean) {
        if (success) {
            showProgress(false,false)
            view?.let{
                Navigation.findNavController(it).navigate(R.id.goToHomeFragment)
            }
        } else {
            showProgress(false,false)

        }
    }


}
