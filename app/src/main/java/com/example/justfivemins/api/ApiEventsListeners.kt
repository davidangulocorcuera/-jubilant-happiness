package com.example.justfivemins.api

import com.example.justfivemins.api.responses.UserResponse


interface ApiEventsListeners{
    interface LoginListener {
        fun isLogged(success: Boolean)
    }
    interface RegisterListener {
        fun isRegistered(success: Boolean)
    }
    interface UserDataListener {
        fun isUserDataSaved(success: Boolean, userResponse: UserResponse)
    }
    interface LocationDataListener {
        fun isLocationUpdated(success: Boolean)
    }
    interface UpdateUserListener {
        fun isUserUpdated(success: Boolean)
    }
    interface OnDataChangedListener{
        fun isUserDataChanged(success: Boolean, userResponse: UserResponse)
    }
}

