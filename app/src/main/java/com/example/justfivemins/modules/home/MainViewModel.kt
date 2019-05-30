package com.example.justfivemins.modules.home

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser

class MainViewModel() : ViewModel(),
    ApiEventsListeners.OnDataChangedListener {
    val picture = MutableLiveData<Uri>()
    val response = MutableLiveData<UserResponse>()
    val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            onUserDataChangedListenerListener = this
        )
    }
    val userResponse: MutableLiveData<UserResponse> by lazy {
        MutableLiveData<UserResponse>().also {
            response
        }
    }

    private val profileImage: MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>().also {
            picture
        }
    }

    fun getPicture(): LiveData<Uri> {
        return profileImage
    }

    fun listenUserData() {
        firebaseApiManager.onUserDataChanged(CurrentUser.firebaseUser!!.uid)

    }

    override fun isUserDataChanged(success: Boolean, userResponse: UserResponse) {
        if (success) {
            response.postValue(userResponse)
        }
    }

}