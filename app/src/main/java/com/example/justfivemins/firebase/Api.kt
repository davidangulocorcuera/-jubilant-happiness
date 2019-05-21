package com.example.justfivemins.firebase

import android.app.Activity

interface Api {
    fun createUser(email: String, password: String, activity: Activity)
}