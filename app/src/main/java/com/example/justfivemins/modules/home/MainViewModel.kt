package com.example.justfivemins.modules.home

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.filesManager.FilesEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.firebase.FirebaseFilesManager
import com.example.justfivemins.api.requests.LocationRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel(),
    ApiEventsListeners.OnUserDataChangedListener,ApiEventsListeners.OnDataChangedListener{


    val url = MutableLiveData<String>()
    val response = MutableLiveData<UserResponse>()
    val users = MutableLiveData<ArrayList<User>>()
    val usersUpdatedResponse = MutableLiveData<ArrayList<User>>()
    val locationRequest = MutableLiveData<LocationRequest>()


    val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            onUserUserDataChangedListener = this,
            onDataChangedListener = this
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
            users
        }
    }
    val usersUpdated: MutableLiveData<ArrayList<User>> by lazy {
        MutableLiveData<ArrayList<User>>().also {
            usersUpdatedResponse
        }
    }

    val profileImageUrl: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            url
        }
    }

    fun listenUserData() {
        CurrentUser.firebaseUser?.let {
            firebaseApiManager.onUserDataChanged(it.uid)
        }

    }

    fun listenUsersData() {
        CurrentUser.firebaseUser?.let {
            firebaseApiManager.onDataChanged()
        }

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

     fun uploadProfileImage(img: Bitmap , listener: FilesEventsListeners.UploadProfileImageListener) {
        val firebaseFilesManager = FirebaseFilesManager(listener)
        firebaseFilesManager.uploadProfileImage(img, CurrentUser.firebaseUser!!.uid,"justFiveMinsProfileImage")
    }


    override fun isUserDataChanged(success: Boolean, userResponse: UserResponse) {
        if (success) {
            response.postValue(userResponse)
        }
    }

    override fun isDataChanged(success: Boolean, users: ArrayList<UserResponse>) {
        if (success) {
            this.usersUpdatedResponse.postValue(setUsersList(users))
        }
    }

    private fun setUsersList(response: ArrayList<UserResponse>): ArrayList<User> {
        var unknownUser: User = User()
        val usersList: ArrayList<User> = ArrayList()
        response.forEach { userResponse ->
            unknownUser.name = userResponse.name
            unknownUser.email = userResponse.email
            unknownUser.birthday = userResponse.birthday
            unknownUser.currentLocation = userResponse.location
            unknownUser.age = userResponse.age

            unknownUser.surname = userResponse.surname
            unknownUser.jobName = userResponse.job
            unknownUser.universityName = userResponse.university
            unknownUser.description = userResponse.description
            unknownUser.profileImageUrl = userResponse.profileImageUrl


            usersList.add(unknownUser)
            unknownUser = User()
        }
        return usersList
    }


}