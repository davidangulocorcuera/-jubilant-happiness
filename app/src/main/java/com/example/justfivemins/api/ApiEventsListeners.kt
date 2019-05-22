package com.example.justfivemins.api

import com.example.justfivemins.api.responses.UserResponse


interface ApiEventsListeners{
    interface LoginListener {
        fun isLogin(success: Boolean)
    }
    interface RegisterListener {
        fun isRegister(success: Boolean)
    }
    interface UserDataListener {
        fun isUserDataSaved(success: Boolean, userResponse: UserResponse)
    }
    interface LocationDataListener {
        fun isLocationUpdated(success: Boolean)
    }
}

