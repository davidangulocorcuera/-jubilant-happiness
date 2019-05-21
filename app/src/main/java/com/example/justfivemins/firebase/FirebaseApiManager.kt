package com.example.justfivemins.firebase

import android.app.Activity
import android.util.Log
import com.example.justfivemins.modules.register.RegisterRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseApiManager : Api {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()


    override fun createUser(registerRequest: RegisterRequest, password: String, activity: Activity) {
        auth.createUserWithEmailAndPassword(registerRequest.email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {

                        db.collection("users").document(user.uid)
                            .set(Mapper.registerRequestMapper(registerRequest))
                            .addOnSuccessListener {
                                Log.v("taag", "goood")
                            }
                            .addOnFailureListener { e ->
                                Log.v("taag", e.toString())
                            }
                    }
                } else {
                    Log.v("FirebaseApiManager", task.exception.toString())
                }
            }


    }


    companion object {
        fun authInstance(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}