package com.example.justfivemins.modules.home

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.LocationRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import java.util.*

class MainViewModel : ViewModel(),
    ApiEventsListeners.OnDataChangedListener{
    val picture = MutableLiveData<Bitmap>()
    val response = MutableLiveData<UserResponse>()
    val users = MutableLiveData<ArrayList<User>>()
    val locationRequest = MutableLiveData<LocationRequest>()


    val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            onUserDataChangedListenerListener = this
        )
    }
    val locationMutable: MutableLiveData<LocationRequest> by lazy {
        MutableLiveData<LocationRequest>().also {
            locationRequest
        }
    }
    val userResponse: MutableLiveData<UserResponse> by lazy {
        MutableLiveData<UserResponse>().also {
            response
        }
    }
    val usersResponse: MutableLiveData<ArrayList<User>> by lazy {
        MutableLiveData<ArrayList<User>>().also {
            response
        }
    }

    private val profileImage: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>().also {
            picture
        }
    }


    fun listenUserData() {
        CurrentUser.firebaseUser?.let {
            firebaseApiManager.onUserDataChanged(it.uid)
        }

    }

    override fun isUserDataChanged(success: Boolean, userResponse: UserResponse) {
        if (success) {
            response.postValue(userResponse)
        }
    }

    fun getRandomLocation(context: Context) {
        val location = LocationRequest()
        val geoCoder = Geocoder(context, Locale.getDefault())
        var addresses = geoCoder.getFromLocation(getRandomLat(), getRandomLon(), 1)


            while (addresses == null
                || addresses.isEmpty()
                || addresses[0].countryName == null
            ) {
                addresses = geoCoder.getFromLocation(getRandomLat(), getRandomLon(), 1)
            }

        location.city = ""
        location.country = addresses[0].countryName
        location.postalCode = ""
        location.lat = addresses[0].latitude
        location.lng = addresses[0].longitude
        locationRequest.postValue(location)
    }

    fun getAddressFromCoordinates(lat: Double, lon: Double,context: Context) {
        val location = LocationRequest()
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>

        addresses = geocoder.getFromLocation(lat, lon, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        if (addresses != null
            && addresses.isNotEmpty()
            && addresses[0].locality != null
            && addresses[0].postalCode != null
            && addresses[0].countryName != null) {

            location.city = addresses[0].locality
            location.country = addresses[0].countryName
            location.postalCode = addresses[0].postalCode
            location.lat = addresses[0].latitude
            location.lng = addresses[0].longitude
        }
        locationRequest.postValue(location)


    }

    fun updateLocation(data: LocationRequest, activity: Activity, listener: ApiEventsListeners.LocationDataListener) {
        val firebaseApiManager: FirebaseApiManager by lazy {
            FirebaseApiManager(
                locationUpdateListener = listener ,
                activity = activity
            )
        }
        firebaseApiManager.updateLocation(data, CurrentUser.firebaseUser!!.uid)
    }

    private fun getRandomLat(): Double {
        val r = Random()
        val i = 0 + r.nextDouble() * (90 - 0)
        return i
    }

    fun getRandomLon(): Double {
        val r = Random()
        return 0 + r.nextDouble() * (180 - 0)
    }



}