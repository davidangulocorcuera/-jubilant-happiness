package com.example.justfivemins.firebase

import android.app.Activity
import com.example.justfivemins.modules.responses.UserResponse
import com.example.justfivemins.requests.RegisterRequest
import com.google.firebase.auth.FirebaseUser

interface Api {
    fun createUser(registerRequest: RegisterRequest, password: String, activity: Activity)
    fun getUserData(currentUser: FirebaseUser): UserResponse
}