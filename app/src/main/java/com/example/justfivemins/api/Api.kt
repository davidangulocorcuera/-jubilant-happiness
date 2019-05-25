package com.example.justfivemins.api

import com.example.justfivemins.api.requests.LocationRequest
import com.example.justfivemins.api.requests.LoginRequest
import com.example.justfivemins.api.requests.RegisterRequest
import com.example.justfivemins.api.requests.UpdateUserRequest
import com.google.firebase.auth.FirebaseUser

interface Api {
     fun createUser(registerRequest: RegisterRequest)
     fun getUserData(currentUser: FirebaseUser)
     fun loginUser(loginRequest: LoginRequest)
     fun updateLocation(locationRequest: LocationRequest, userId: String)
     fun updateUserData(updateUserRequest: UpdateUserRequest, userId: String)
}