package com.example.justfivemins.api

import com.example.justfivemins.api.requests.LocationRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.api.requests.RegisterRequest
import com.google.firebase.firestore.DocumentSnapshot


object Mapper {
    fun registerRequestMapper(registerRequest: RegisterRequest) : Map<String, Any> {
        val data = HashMap<String, Any>()
        registerRequest.run {
            data["name"] = name
            data["surname"] = surname
            data["email"] = email
            return data
        }
    }
    fun userResponseMapper(documentSnapshot: DocumentSnapshot) : UserResponse {
        val userResponse = UserResponse()
        documentSnapshot.data.let {
            userResponse.name = it?.get("name") as String
            userResponse.surname = it?.get("surname") as String
            userResponse.email = it?.get("email") as String
        }
        return userResponse
    }
    fun locationRequestMapper(locationRequest: LocationRequest) : Map<String, Any> {
        val data = HashMap<String, Any>()
        locationRequest.run {
            data["city"] = city
            data["country"] = country
            data["postalCode"] = postalCode
            data["lat"] = lat
            data["lng"] = lng
            return data
        }
    }
}