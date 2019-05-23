package com.example.justfivemins.modules.map


import android.annotation.SuppressLint
import android.location.Geocoder
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.justfivemins.MainActivity
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.BaseFragment
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_location_dialog.*
import org.jetbrains.anko.find
import java.util.*


class LocationFragment : BaseFragment(), ApiEventsListeners.LocationDataListener, PermissionsListener.LocationListener{


    override fun onLocationPermissionResponse(isAccepted: Boolean){
        val btn: Button = view!!.findViewById(R.id.btnNext)
        btn.isEnabled = isAccepted
    }

    private val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(locationUpdateListener = this) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateViewId(): Int {
        return R.layout.fragment_location_dialog
    }


    @SuppressLint("MissingPermission")
    override fun viewCreated(view: View?) {
        val permissionListener: MainActivity  = MainActivity(fragment = this)

        if (permissionListener.validatePermissionsLocation()) {
            btnNext.isEnabled = true
        } else {
            permissionListener.permissionRequest()
        }

        btnNext.setOnClickListener {
            showProgress(show = true, hasShade = true)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                updateLocation(getLocationFromCoordinates(it.latitude, it.longitude))
            }
        }
    }

    private fun getLocationFromCoordinates(lat: Double, lng: Double): com.example.justfivemins.api.requests.LocationRequest {
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
