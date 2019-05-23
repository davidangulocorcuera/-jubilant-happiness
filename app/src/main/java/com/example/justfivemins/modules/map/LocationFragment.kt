package com.example.justfivemins.modules.map



import android.location.Geocoder
import android.view.View
import android.widget.Toast
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.BaseActivity
import com.example.justfivemins.modules.base.BaseFragment
import com.google.android.gms.location.LocationResult
import java.util.*


class LocationFragment : BaseFragment(), ApiEventsListeners.LocationDataListener {

    private val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(locationUpdateListener = this) }
    var location: Location? = null


    override fun onCreateViewId(): Int {
        return R.layout.fragment_location_dialog
    }

    override fun viewCreated(view: View?) {
        showProgress(show = true, hasShade = true)
        location = Location(this.activity as BaseActivity, object : LocationListener {
            override fun locationResponse(locationResult: LocationResult) {
                updateLocation(getLocationFromCoordinates(locationResult.lastLocation.latitude,locationResult.lastLocation.longitude))
            }
        })
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


    override fun onStart() {
        super.onStart()
        location?.inicializeLocation()
    }

    override fun onPause() {
        super.onPause()
        location?.stopUpdateLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        location?.onRequestPermissionsResult(requestCode, permissions, grantResults)

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
