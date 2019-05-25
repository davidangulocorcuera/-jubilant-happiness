package com.example.justfivemins.api

import android.util.Log
import com.example.justfivemins.api.requests.RegisterRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.Location
import com.example.justfivemins.model.User
import com.google.firebase.firestore.DocumentSnapshot


object Mapper {
    fun registerRequestMapper(registerRequest: RegisterRequest): Map<String, Any> {
        val data = HashMap<String, Any>()
        registerRequest.run {
            data["name"] = name
            data["birthday"] = birthday
            data["email"] = email
            data["age"] = age
            data["gender"] = gender

            data["job"] = ""
            data["university"] = ""
            data["description"] = ""
            data["surname"] = ""
            return data
        }
    }

    fun userResponseMapper(documentSnapshot: DocumentSnapshot): UserResponse {
        val userResponse = UserResponse()
        val location = Location()


        documentSnapshot.data.let {
            userResponse.name = it?.get("name") as String
            userResponse.birthday = it?.get("birthday") as String
            userResponse.email = it?.get("email") as String
            userResponse.age = Integer.parseInt(it?.get("age").toString())
            userResponse.gender = User.Gender.valueOf( it["gender"].toString())
            userResponse.job = it?.get("job") as String
            userResponse.university = it?.get("university") as String
            userResponse.surname = it?.get("surname") as String
            userResponse.description = it?.get("description") as String


            if(documentSnapshot.get("location") !=  null){
                val locationMap: HashMap<String, Any> = documentSnapshot.get("location") as HashMap<String, Any>
                if(locationMap.size > 0 ){
                    location.lng = locationMap["lng"] as Double
                    location.lat = locationMap["lat"] as Double
                    location.postalCode = locationMap["postalCode"] as String
                    location.city = locationMap["city"] as String
                    location.country = locationMap["country"] as String
                }
            }
            userResponse.location = location
        }
        return userResponse
    }

}