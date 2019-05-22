package com.example.justfivemins.api

import android.app.Activity
import com.example.justfivemins.api.requests.LocationRequest
import com.example.justfivemins.api.requests.LoginRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.api.requests.RegisterRequest
import com.example.justfivemins.model.Location
import com.google.firebase.auth.FirebaseUser

interface Api {
     fun createUser(registerRequest: RegisterRequest, password: String, activity: Activity)
     fun getUserData(currentUser: FirebaseUser)
     fun loginUser(loginRequest: LoginRequest, activity: Activity)
     fun updateLocation(locationRequest: LocationRequest, activity: Activity, currentUser: FirebaseUser)
}