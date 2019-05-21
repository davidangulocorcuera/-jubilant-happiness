package com.example.justfivemins.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth


class FirebaseApiManager : Api {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun createUser(email: String, password: String, activity: Activity) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.v("FirebaseApiManager", user?.uid.toString())
                } else {
                    Log.v("FirebaseApiManager", task.exception.toString())
                }
            }

    }


    companion object {
        fun authInstance(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}