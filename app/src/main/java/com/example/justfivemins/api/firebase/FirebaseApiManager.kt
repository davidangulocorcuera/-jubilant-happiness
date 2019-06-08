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
    , private val onUserUserDataChangedListener: ApiEventsListeners.OnUserDataChangedListener? = null
    , private val onDataChangedListener: ApiEventsListeners.OnDataChangedListener? = null
    , private val onGetUsersListener: ApiEventsListeners.GetUsersListener? = null
    , private val activity: Activity? = null
) : Api {


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    private val chatChannelsColectionRef = db.collection("chatChannels")
    private val currentUserDocRef: DocumentReference
        get() = db.document(
            "users/${FirebaseAuth.getInstance().currentUser?.uid ?: throw NullPointerException("UID is null")}"
        )


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
                    onUserUserDataChangedListener?.isUserDataChanged(false, UserResponse())
                    return
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = Mapper.userResponseMapper(snapshot.data!!)
                    onUserUserDataChangedListener?.isUserDataChanged(true, user)

                } else {
                    onUserUserDataChangedListener?.isUserDataChanged(false, UserResponse())
                }
            }
        })
    }

    override fun onDataChanged() {
        val docRef = db.collection("users")
        docRef.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                @Nullable snapshot: QuerySnapshot?,
                @Nullable e: FirebaseFirestoreException?
            ) {
                if (e != null) {
                    onDataChangedListener?.isDataChanged(false, ArrayList())
                    return
                }
                if (snapshot != null) {
                    onDataChangedListener?.isDataChanged(true, Mapper.mapAllUsers(snapshot))

                } else {
                    onDataChangedListener?.isDataChanged(false, ArrayList())
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

    override fun getUserData(currentUser: FirebaseUser) {
        val docRef = db.collection("users").document(currentUser.uid)
        var user = UserResponse()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = Mapper.userResponseMapper(document.data!!)
                    userDataListener?.isUserDataSaved(true, user)

                } else {
                    Log.d("taag", "No such document")
                    userDataListener?.isUserDataSaved(false, UserResponse())

                }
            }
            .addOnFailureListener { exception ->
                Log.d("taag", "get failed with ", exception)
                userDataListener?.isUserDataSaved(false, UserResponse())

            }
    }

    fun getOrCreateChatChannel(
        otherUserId: String,
        onComplete: (channelId: String) -> Unit
    ) {
        currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                if(it.exists()){
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }
                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                val newChannel = chatChannelsColectionRef.document()
                newChannel.set(ChatChannel(mutableListOf(currentUserId,otherUserId)))

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

    fun addChatMessagesListener(channelId: String, context: Context, onListen: (List<TextMessageItem>) -> Unit): ListenerRegistration{

            return chatChannelsColectionRef.document(channelId).collection("messages")
                .orderBy("time")
                .addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                    if(firebaseFirestoreException != null){
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<TextMessageItem>()

                    querySnapshot?.documents?.forEach{
                        items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!,context))
                    }
                    onListen(items)
                }
    }

    fun sendMessage(message: Message, channelId: String){
        chatChannelsColectionRef.document(channelId)
            .collection("messages")
            .add(message)

    }


}