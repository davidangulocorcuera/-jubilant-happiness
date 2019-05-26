package com.example.justfivemins.api.firebase

import android.app.Activity
import android.support.annotation.Nullable
import android.util.Log
import com.example.justfivemins.api.Api
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.Mapper
import com.example.justfivemins.api.requests.LocationRequest
import com.example.justfivemins.api.requests.LoginRequest
import com.example.justfivemins.api.requests.RegisterRequest
import com.example.justfivemins.api.requests.UpdateUserRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*


class FirebaseApiManager(
    private val loginListener: ApiEventsListeners.LoginListener? = null
    , private val registerListener: ApiEventsListeners.RegisterListener? = null
    , private val userDataListener: ApiEventsListeners.UserDataListener? = null
    , private val locationUpdateListener: ApiEventsListeners.LocationDataListener? = null
    , private val updateUserListener: ApiEventsListeners.UpdateUserListener? = null
    , private val onUserDataChangedListenerListener: ApiEventsListeners.OnDataChangedListener? = null
    , private val activity: Activity
) : Api {


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()


    override fun createUser(registerRequest: RegisterRequest) {

        auth.createUserWithEmailAndPassword(registerRequest.email, registerRequest.password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        CurrentUser.firebaseUser = user
                        db.collection("users").document(user.uid)
                            .set(Mapper.registerRequestMapper(registerRequest))
                            .addOnFailureListener { e ->
                                registerListener?.isRegistered(false)
                                Log.v("taag", e.toString())
                            }
                        registerListener?.isRegistered(true)
                    }

                } else {
                    Log.v("taag", task.exception.toString())
                    registerListener?.isRegistered(false)

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

    override fun loginUser(loginRequest: LoginRequest) {
        auth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    CurrentUser.firebaseUser = user
                    loginListener?.isLogged(true)
                } else {
                    loginListener?.isLogged(false)
                }

            }
    }

    override fun updateLocation(locationRequest: LocationRequest ,userId: String) {
        db.collection("users").document(userId)
            .update("location", locationRequest)
            .addOnCompleteListener{
                locationUpdateListener?.isLocationUpdated(true)

            }
            .addOnFailureListener {
                locationUpdateListener?.isLocationUpdated(false)
            }

    }

    override fun updateUserData(updateUserRequest: UpdateUserRequest, userId: String) {
        db.collection("users").document(userId)
            .update(
                "surname", updateUserRequest.surname,
                "university", updateUserRequest.university,
                "name", updateUserRequest.name,
                "job", updateUserRequest.job,
                "description", updateUserRequest.description
            ).addOnCompleteListener {
                updateUserListener?.isUserUpdated(true)
            }.addOnFailureListener {
                updateUserListener?.isUserUpdated(false)
            }
    }
    override fun onUserDataChanged(userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.addSnapshotListener(object : EventListener<DocumentSnapshot> {
           override fun onEvent(
                @Nullable snapshot: DocumentSnapshot?,
                @Nullable e: FirebaseFirestoreException?
            ) {
                if (e != null) {
                    onUserDataChangedListenerListener?.isUserDataChanged(false,UserResponse())
                    return
                }

               if (snapshot != null && snapshot.exists()) {
                   val user  = Mapper.userResponseMapper(snapshot)
                   onUserDataChangedListenerListener?.isUserDataChanged(true,user)
                } else {
                   onUserDataChangedListenerListener?.isUserDataChanged(false,UserResponse())
                }
            }
        })
    }



}