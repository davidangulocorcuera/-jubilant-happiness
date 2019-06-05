package com.example.justfivemins.model

import com.google.firebase.auth.FirebaseUser

/**
 * Class for save current user in memory
 * */
object CurrentUser {
    var firebaseUser: FirebaseUser? = null
    var user: User? = null
}