package com.example.justfivemins.api.firebase

import android.app.Activity
import android.util.Log
import com.example.justfivemins.api.Api
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.Mapper
import com.example.justfivemins.api.requests.LocationRequest
import com.example.justfivemins.api.requests.LoginRequest
import com.example.justfivemins.api.requests.RegisterRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore







class FirebaseApiManager(
    private val loginListener: ApiEventsListeners.LoginListener? = null
    , private val registerListener: ApiEventsListeners.RegisterListener? = null
    , private val userDataListener: ApiEventsListeners.UserDataListener? = null
    , private val locationUpdateListener: ApiEventsListeners.LocationDataListener? = null

) : Api {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()


    override fun createUser(registerRequest: RegisterRequest, password: String, activity: Activity) {

        auth.createUserWithEmailAndPassword(registerRequest.email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        CurrentUser.firebaseUser = user
                        db.collection("users").document(user.uid)
                            .set(Mapper.registerRequestMapper(registerRequest))
                            .addOnFailureListener { e ->
                                registerListener?.isRegister(false)
                                Log.v("taag", e.toString())
                            }
                        registerListener?.isRegister(true)
                    }

                } else {
                    Log.v("taag", task.exception.toString())
                    registerListener?.isRegister(false)

                }
            }
    }

    override fun getUserData(currentUser: FirebaseUser) {
        val docRef = db.collection("users").document(currentUser.uid)
        var user = UserResponse()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = Mapper.userResponseMapper(document)
                    userDataListener?.isUserDataSaved(true,user)

                } else {
                    Log.d("taag", "No such document")
                    userDataListener?.isUserDataSaved(false, UserResponse())

                }
            }
            .addOnFailureListener { exception ->
                Log.d("taag", "get failed with ", exception)
                userDataListener?.isUserDataSaved(false ,UserResponse())

            }
    }

    override fun loginUser(loginRequest: LoginRequest, activity: Activity) {
        auth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    CurrentUser.firebaseUser = user
                    loginListener?.isLogin(true)
                } else {
                    loginListener?.isLogin(false)
                }

            }
    }

    override fun updateLocation(locationRequest: LocationRequest, activity: Activity,currentUser: FirebaseUser) {
        db.collection("users").document(currentUser.uid)
            .update("location", locationRequest)
            .addOnCompleteListener{
                locationUpdateListener?.isLocationUpdated(true)

            }
            .addOnFailureListener {
                locationUpdateListener?.isLocationUpdated(false)

            }

    }



}