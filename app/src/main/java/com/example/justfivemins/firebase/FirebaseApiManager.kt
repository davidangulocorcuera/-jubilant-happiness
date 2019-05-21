package com.example.justfivemins.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.login.LoginRequest
import com.example.justfivemins.modules.responses.UserResponse
import com.example.justfivemins.requests.RegisterRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseApiManager : Api, FirebaseListener {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()


    override fun createUser(registerRequest: RegisterRequest, password: String, activity: Activity) {
        auth.createUserWithEmailAndPassword(registerRequest.email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        CurrentUser.user = user
                        db.collection("users").document(user.uid)
                            .set(Mapper.registerRequestMapper(registerRequest))
                            .addOnFailureListener { e ->
                                Log.v("taag", e.toString())
                            }
                    }
                } else {
                    Log.v("taag", task.exception.toString())
                }
            }


    }

    override fun getUserData(currentUser: FirebaseUser): UserResponse {
        val docRef = db.collection("users").document(currentUser.uid)
        var user: UserResponse = UserResponse()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = Mapper.userResponseMapper(document)
                } else {
                    Log.d("taag", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("taag", "get failed with ", exception)
            }
        return user
    }

    override fun loginUser(loginRequest: LoginRequest, activity: Activity) {
        auth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    CurrentUser.user = user

                } else {
                    Toast.makeText(
                        activity.applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }


}