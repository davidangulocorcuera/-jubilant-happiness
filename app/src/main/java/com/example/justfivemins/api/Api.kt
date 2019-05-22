package com.example.justfivemins.api

import android.app.Activity
import com.example.justfivemins.api.requests.LoginRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.api.requests.RegisterRequest
import com.google.firebase.auth.FirebaseUser

interface Api {
     fun createUser(registerRequest: RegisterRequest, password: String, activity: Activity)
     fun getUserData(currentUser: FirebaseUser): UserResponse
     fun loginUser(loginRequest: LoginRequest, activity: Activity)
}