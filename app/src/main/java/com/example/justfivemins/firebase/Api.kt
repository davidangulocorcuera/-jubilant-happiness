package com.example.justfivemins.firebase

import android.app.Activity
import com.example.justfivemins.modules.register.RegisterRequest

interface Api {
    fun createUser(registerRequest: RegisterRequest, password: String, activity: Activity)
}