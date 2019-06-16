package com.example.justfivemins.api.firebase

import android.app.Activity
import android.content.Context
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
import com.example.justfivemins.modules.messages.chat.ChatChannel
import com.example.justfivemins.modules.messages.chat.Message
import com.example.justfivemins.modules.messages.chat.TextMessage
import com.example.justfivemins.modules.messages.chat.TextMessageItem
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*


/**
 * Firebase api
 * */

class FirebaseApiManager(
    private val loginListener: ApiEventsListeners.LoginListener? = null
    , private val registerListener: ApiEventsListeners.RegisterListener? = null
    , private val locationUpdateListener: ApiEventsListeners.LocationDataListener? = null
    , private val updateUserListener: ApiEventsListeners.UpdateUserListener? = null
    , private val userDataListener: ApiEventsListeners.UserDataListener? = null
    , private val onUserDataChangedListener: ApiEventsListeners.OnUserDataChangedListener? = null
    , private val onResetPasswordEmailSentListener: ApiEventsListeners.OnResetPasswordEmailSentListener? = null
    , private val onUserRemovedListener: ApiEventsListeners.OnUserRemovedListener? = null
    , private val onDataChangedListener: ApiEventsListeners.OnDataChangedListener? = null
    , private val onGetUsersListener: ApiEventsListeners.GetUsersListener? = null
    , private val reAuthUserListener: ApiEventsListeners.ReAuthUserListener? = null

    , private val activity: Activity? = null
) : Api {


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    private val chatChannelsColectionRef = db.collection("chatChannels")
    private val currentUserDocRef: DocumentReference
        get() = db.document(
            "users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null") as Throwable}"
        )

    /** Function for create user */
    override fun createUser(registerRequest: RegisterRequest) {
        activity?.let {
            auth.createUserWithEmailAndPassword(registerRequest.email, registerRequest.password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            CurrentUser.firebaseUser = user
                            registerRequest.id = user.uid
                            db.collection("users").document(user.uid)
                                .set(Mapper.registerRequestMapper(registerRequest))
                                .addOnFailureListener { e ->
                                    registerListener?.isRegistered(false)
                                    /** ERROR */
                                }
                            registerListener?.isRegistered(true)
                            user.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        /** Verification email sended!*/
                                    }
                                }
                        }

                    } else {
                        /** ERROR */
                        registerListener?.isRegistered(false)

                    }
                }
        }

    }

    /** Function for auth user */
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

    /** Function for update user location */
    override fun updateLocation(locationRequest: LocationRequest, userId: String) {
        if(locationRequest.city.isNotEmpty()){
            db.collection("users").document(userId)
                .update("location", locationRequest)
                .addOnCompleteListener {
                    locationUpdateListener?.isLocationUpdated(true)

                }
                .addOnFailureListener {
                    /** ERROR */
                    locationUpdateListener?.isLocationUpdated(false)
                }
        }
        else{
            locationUpdateListener?.isLocationUpdated(false)
        }
    }

    /** Function for update user data */
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
                /** ERROR */
            }
    }

    /** Function for listen changes in current user */
    override fun onUserDataChanged(userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(
                @Nullable snapshot: DocumentSnapshot?,
                @Nullable e: FirebaseFirestoreException?
            ) {
                if (e != null) {
                    onUserDataChangedListener?.isUserDataChanged(false, UserResponse())
                    /** ERROR */
                    return
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = Mapper.userResponseMapper(snapshot.data!!)
                    onUserDataChangedListener?.isUserDataChanged(true, user)

                } else {
                    onUserDataChangedListener?.isUserDataChanged(false, UserResponse())
                    /** ERROR */
                }
            }
        })
    }

    /** Function for listen changes in all users */
    override fun onDataChanged() {
        val docRef = db.collection("users")
        docRef.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                @Nullable snapshot: QuerySnapshot?,
                @Nullable e: FirebaseFirestoreException?
            ) {
                if (e != null) {
                    onDataChangedListener?.isDataChanged(false, ArrayList())
                    /** ERROR */
                    return
                }
                if (snapshot != null) {
                    onDataChangedListener?.isDataChanged(true, Mapper.mapAllUsers(snapshot))

                } else {
                    onDataChangedListener?.isDataChanged(false, ArrayList())
                    /** ERROR */
                }
            }
        })
    }

    /** Function for get users collection */
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
                    /** ERROR */
                    onGetUsersListener?.areUsersSaved(false, ArrayList())

                }
            }
            .addOnFailureListener { exception ->
                /** ERROR */
                onGetUsersListener?.areUsersSaved(false, ArrayList())

            }
    }

    override fun getUserData(currentUser: FirebaseUser) {
        val docRef = db.collection("users").document(currentUser.uid)
        var user = UserResponse()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = Mapper.userResponseMapper(document.data!!)
                    userDataListener?.isUserDataSaved(true, user)

                } else {
                    /** ERROR */
                    userDataListener?.isUserDataSaved(false, UserResponse())

                }
            }
            .addOnFailureListener { exception ->
                /** ERROR */
                userDataListener?.isUserDataSaved(false, UserResponse())

            }
    }

    /** Chat need to be implemented, just working for tests*/
    fun getOrCreateChatChannel(
        otherUserId: String,
        onComplete: (channelId: String) -> Unit
    ) {
        currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }
                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                val newChannel = chatChannelsColectionRef.document()
                newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                currentUserDocRef.collection("engagedChatChannels")
                    .document(otherUserId)
                    .set(mapOf("channelId" to newChannel.id))

                db.collection("users")
                    .document(otherUserId)
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)

            }
    }

    /** Chat need to be implemented, just working for tests*/
    fun addChatMessagesListener(
        channelId: String,
        context: Context,
        onListen: (List<TextMessageItem>) -> Unit
    ): ListenerRegistration {

        return chatChannelsColectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    return@addSnapshotListener
                }

                val items = mutableListOf<TextMessageItem>()

                querySnapshot?.documents?.forEach {
                    items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context))
                }
                onListen(items)
            }
    }

    /** Chat need to be implemented, just working for tests*/
    fun sendMessage(message: Message, channelId: String) {
        chatChannelsColectionRef.document(channelId)
            .collection("messages")
            .add(message)

    }

    /** Function for send an e-mail to the user when he will change the password*/
    override fun sendPasswordEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResetPasswordEmailSentListener?.isEmailSent(true)
                }
            }
            .addOnFailureListener {
                onResetPasswordEmailSentListener?.isEmailSent(false)
                /** ERROR */
            }
    }

    /** Function for remove user and all the data from the user*/
    override fun removeUser() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("users").document(it.uid).delete()
            it.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirebaseAuth.getInstance().signOut()
                        onUserRemovedListener?.isUserRemoved(true)
                    }
                }
                .addOnFailureListener {
                    onUserRemovedListener?.isUserRemoved(false)
                    /** ERROR */
                }
        }

    }

    /** Function for revalidate user token */
    override fun reAuthUser(request: String) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            it.email?.let { email ->
                val credential = EmailAuthProvider
                    .getCredential(email, request)
                it.reauthenticate(credential)
                    ?.addOnCompleteListener {
                        reAuthUserListener?.isUserReAuth(true)
                    }
                    ?.addOnFailureListener{
                        reAuthUserListener?.isUserReAuth(false)
                    }
            }

        }
    }


}