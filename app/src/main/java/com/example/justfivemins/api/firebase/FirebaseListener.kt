package com.example.justfivemins.api.firebase

interface FirebaseListener{
    interface LoginListener {
        fun isLoginOk(success: Boolean)
    }
    interface RegisterListener {
        fun isRegisterOk(success: Boolean)
    }
    interface UserDataListener {
        fun isUserDataOk(success: Boolean)
    }
}

