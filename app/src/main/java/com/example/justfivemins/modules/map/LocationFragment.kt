package com.example.justfivemins.modules.map


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
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_location_dialog.*
import java.util.*




class LocationFragment : BaseFragment(), ApiEventsListeners.LocationDataListener {

    private val permissionFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permissionCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val REQUEST_CODE_LOCATION = 100
    private val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(locationUpdateListener = this) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateViewId(): Int {
        return R.layout.fragment_location_dialog
    }

    @SuppressLint("MissingPermission")
    override fun viewCreated(view: View?) {

        if (validatePermissionsLocation()) {
            btnNext.isEnabled = true
        } else {
            permissionRequest()
        }

        btnNext.setOnClickListener {
            showProgress(show = true, hasShade = true)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                updateLocation(getLocationFromCoordinates(it.latitude, it.longitude))
            }
        }
    }


    fun getLocationFromCoordinates(lat: Double, lng: Double): com.example.justfivemins.api.requests.LocationRequest {
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

    private fun validatePermissionsLocation(): Boolean {
        val fineLocationAvailable = ActivityCompat.checkSelfPermission(activity!!.applicationContext, permissionFineLocation) == PackageManager.PERMISSION_GRANTED
        val coarseLocationAvailable = ActivityCompat.checkSelfPermission(activity!!.applicationContext,permissionCoarseLocation) == PackageManager.PERMISSION_GRANTED
        return fineLocationAvailable && coarseLocationAvailable
    }

    private fun permissionRequest() {
        ActivityCompat.requestPermissions(activity!!,arrayOf(permissionFineLocation, permissionCoarseLocation),REQUEST_CODE_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnNext.isEnabled = true
                } else {
                    Toast.makeText(
                        activity!!.applicationContext,
                        "You did not give permissions to get location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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

}
