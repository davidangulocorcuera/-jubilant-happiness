package com.example.justfivemins.firebase

import com.example.justfivemins.modules.responses.UserResponse
import com.example.justfivemins.requests.RegisterRequest
import com.google.firebase.firestore.DocumentSnapshot


object Mapper {
    fun registerRequestMapper(registerRequest: RegisterRequest) : Map<String, Any> {
        val data = HashMap<String, Any>()
        data["name"] = registerRequest.name
        data["surname"] = registerRequest.surname
        data["email"] = registerRequest.email
        return data
    }
    fun userResponseMapper(documentSnapshot: DocumentSnapshot) : UserResponse {
        val userResponse = UserResponse()
        userResponse.name = documentSnapshot.data?.get("name") as String
        userResponse.surname = documentSnapshot.data?.get("surname") as String
        userResponse.email = documentSnapshot.data?.get("email") as String
        return userResponse
    }
}