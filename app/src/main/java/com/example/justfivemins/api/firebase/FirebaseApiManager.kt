package com.example.justfivemins.api.firebase

import android.app.Activity
import android.util.Log
import com.example.justfivemins.api.Api
import com.example.justfivemins.api.Mapper
import com.example.justfivemins.api.requests.LoginRequest
import com.example.justfivemins.api.requests.RegisterRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseApiManager(
    private val loginListener: FirebaseListener.LoginListener? = null
    , private val registerListener: FirebaseListener.RegisterListener? = null
    , private val userDataListener: FirebaseListener.UserDataListener? = null
) : Api {

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
                                registerListener?.isRegisterOk(false)
                                Log.v("taag", e.toString())
                            }
                        registerListener?.isRegisterOk(true)


                    }

                } else {
                    Log.v("taag", task.exception.toString())
                    registerListener?.isRegisterOk(false)

                }
            }
    }

    override fun getUserData(currentUser: FirebaseUser): UserResponse {
        val docRef = db.collection("users").document(currentUser.uid)
        var user = UserResponse()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = Mapper.userResponseMapper(document)
                    userDataListener?.isUserDataOk(true,user)

                } else {
                    Log.d("taag", "No such document")
                    userDataListener?.isUserDataOk(false, UserResponse())

                }
            }
            .addOnFailureListener { exception ->
                Log.d("taag", "get failed with ", exception)
                userDataListener?.isUserDataOk(false ,UserResponse())

            }
        return user
    }

    override fun loginUser(loginRequest: LoginRequest, activity: Activity) {
        auth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    CurrentUser.user = user
                    loginListener?.isLoginOk(true)
                } else {
                    loginListener?.isLoginOk(false)
                }

            }
    }

}