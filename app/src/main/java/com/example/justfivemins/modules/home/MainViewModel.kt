package com.example.justfivemins.modules.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val picture = MutableLiveData<Uri>()


    private val profileImage: MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>().also {
            picture
        }
    }

    fun getPicture(): LiveData<Uri> {
        return profileImage
    }

    private fun loadPicture() {
        // Do an asynchronous operation to fetch users.
    }
}