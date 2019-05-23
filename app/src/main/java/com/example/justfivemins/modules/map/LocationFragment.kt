package com.example.justfivemins.modules.map


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.support.v4.app.ActivityCompat
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
import kotlinx.android.synthetic.main.fragment_location_dialog.*
import java.util.*


class LocationFragment : BaseFragment(), ApiEventsListeners.LocationDataListener {
    private val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(locationUpdateListener = this) }


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val permissionFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permissionCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val REQUEST_CODE_LOCATION = 100


    override fun onCreateViewId(): Int {
        return R.layout.fragment_location_dialog
    }


    @SuppressLint("MissingPermission")
    override fun viewCreated(view: View?) {
        btnNext.setOnClickListener {
            if (validatePermissionsLocation()) {
                getLocation()
            } else {
                requestPermissions()
            }
        }


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

    private fun updateLocation(data: com.example.justfivemins.api.requests.LocationRequest) =
        firebaseApiManager.updateLocation(
            data,
            activity!!,
            CurrentUser.firebaseUser!!
        )

    fun validatePermissionsLocation(): Boolean {
        val fineLocationAvailable =
            ActivityCompat.checkSelfPermission(
                context!!,
                permissionFineLocation
            ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationAvailable =
            ActivityCompat.checkSelfPermission(
                context!!,
                permissionCoarseLocation
            ) == PackageManager.PERMISSION_GRANTED
        return fineLocationAvailable && coarseLocationAvailable
    }


    fun requestPermissions() {

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


    override fun isLocationUpdated(success: Boolean) {
        showProgress(show = false, hasShade = false)
        if (success) {
            navigator.addBackStack(false).navigateToHome()
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


}
