package com.example.justfivemins.modules.home

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.LocationRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel(),
    ApiEventsListeners.OnDataChangedListener ,ApiEventsListeners.LocationDataListener{
    val picture = MutableLiveData<Uri>()
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

    private val profileImage: MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>().also {
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
    override fun isLocationUpdated(success: Boolean) {
        if (success) {
        } else {
        }
    }
     fun getRandomLocation(context: Context) {
        val location = LocationRequest()
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(getRandomLat(), getRandomLon(), 1)
        if (addresses != null && addresses.size > 0) {
            location.city = if (addresses[0].locality != null) addresses[0].locality else ""
            location.country = if (addresses[0].countryName != null) addresses[0].countryName else "Country not found"
            location.postalCode = if (addresses[0].postalCode != null) addresses[0].postalCode else ""
            location.lat = addresses[0].latitude
            location.lng = addresses[0].longitude
            locationRequest.postValue(location)

        }


     }
     fun updateLocation(data: LocationRequest,activity: Activity) {
        val firebaseApiManager: FirebaseApiManager by lazy {
            FirebaseApiManager(
                locationUpdateListener = this,
                activity = activity
            )
        }
        firebaseApiManager.updateLocation(data, CurrentUser.firebaseUser!!.uid)
    }
    fun getRandomLat(): Double{
        val r = Random()
        val i =  0 + r.nextDouble() * (90 - 0)
        return i
    }
    fun getRandomLon(): Double{
        val r = Random()
        return  0 + r.nextDouble() * (180 - 0)
    }

}