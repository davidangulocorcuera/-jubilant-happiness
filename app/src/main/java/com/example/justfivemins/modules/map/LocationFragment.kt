package com.example.justfivemins.modules.map


import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.view.View
import android.widget.Toast
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.BaseFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*


class LocationFragment : BaseFragment(), ApiEventsListeners.LocationDataListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateViewId(): Int {
        return R.layout.fragment_location
    }


    @SuppressLint("MissingPermission")
    override fun viewCreated(view: View?) {
        btnNext.setOnClickListener {
            requestLocationPermissions()
        }
        hideToolbar()

    }

    private fun getLocationFromCoordinates(
        lat: Double,
        lng: Double
    ): com.example.justfivemins.api.requests.LocationRequest {
        val location: com.example.justfivemins.api.requests.LocationRequest =
            com.example.justfivemins.api.requests.LocationRequest()
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(lat, lng, 1)
        if (addresses != null && addresses.size > 0) {
            location.city = addresses[0].locality
            location.country = addresses[0].countryName
            location.postalCode = addresses[0].postalCode
            location.lat = lat
            location.lng = lng
        }
        return location
    }

    private fun updateLocation(data: com.example.justfivemins.api.requests.LocationRequest) {
        val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(locationUpdateListener = this, activity = activity!!) }
        firebaseApiManager.updateLocation(data, CurrentUser.firebaseUser!!.uid)
    }

    private fun requestLocationPermissions() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        getLocation()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    override fun onResume() {
        super.onResume()
        showProgress(show = false, hasShade = false)

    }
    override fun isLocationUpdated(success: Boolean) {
        if (success) {
            navigator.finishCurrent(true).navigateToHome()
        } else {
            Toast.makeText(
                activity?.applicationContext, "update failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        showProgress(show = true, hasShade = true)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            updateLocation(getLocationFromCoordinates(it.latitude, it.longitude))
        }
    }

    override fun hasToolbar(): Boolean {
        return false
    }


}
