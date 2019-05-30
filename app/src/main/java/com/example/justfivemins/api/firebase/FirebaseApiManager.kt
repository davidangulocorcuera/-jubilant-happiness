package com.example.justfivemins.api.firebase

import android.app.Activity
import android.util.Log
import androidx.annotation.Nullable
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException


class FirebaseApiManager(
    private val loginListener: ApiEventsListeners.LoginListener? = null
    , private val registerListener: ApiEventsListeners.RegisterListener? = null
    , private val locationUpdateListener: ApiEventsListeners.LocationDataListener? = null
    , private val updateUserListener: ApiEventsListeners.UpdateUserListener? = null
    , private val onUserDataChangedListenerListener: ApiEventsListeners.OnDataChangedListener? = null
    , private val onGetUsersListener: ApiEventsListeners.GetUsersListener? = null
    , private val activity: Activity? = null
) : Api {


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()


    override fun createUser(registerRequest: RegisterRequest) {
        activity?.let {
            auth.createUserWithEmailAndPassword(registerRequest.email, registerRequest.password)
                .addOnCompleteListener(it) { task ->
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

    }

    override fun loginUser(loginRequest: LoginRequest) {
        activity?.let {
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

    }

    override fun updateLocation(locationRequest: LocationRequest, userId: String) {
        db.collection("users").document(userId)
            .update("location", locationRequest)
            .addOnCompleteListener {
                locationUpdateListener?.isLocationUpdated(true)

            }
            .addOnFailureListener {
                Log.v("errortag", it.localizedMessage)
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
                "description", updateUserRequest.description,
                "profileImageUrl", updateUserRequest.profileImageUrl
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
                    onUserDataChangedListenerListener?.isUserDataChanged(false, UserResponse())
                    return
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = Mapper.userResponseMapper(snapshot.data!!)
                    onUserDataChangedListenerListener?.isUserDataChanged(true, user)

                } else {
                    onUserDataChangedListenerListener?.isUserDataChanged(false, UserResponse())
                }
            }
        })
    }

    override fun getAllUsers() {
        val docRef = db.collection("users")

        docRef.get()
            .addOnSuccessListener { document ->


                if (document != null) {
                    onGetUsersListener?.areUsersSaved(
                        true,
                        Mapper.mapAllUsers(document)
                    )

                } else {
                    Log.d("taag", "No such document")
                    onGetUsersListener?.areUsersSaved(false, ArrayList())

                }
            }
            .addOnFailureListener { exception ->
                Log.d("taag", "get failed with ", exception)
                onGetUsersListener?.areUsersSaved(false, ArrayList())

            }

    }


}