package com.example.justfivemins.api

import com.example.justfivemins.api.requests.RegisterRequest
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.Location
import com.example.justfivemins.model.User
import com.google.firebase.firestore.QuerySnapshot


object Mapper {
    fun registerRequestMapper(registerRequest: RegisterRequest): Map<String, Any> {
        val data = HashMap<String, Any>()
        registerRequest.run {
            data["name"] = name
            data["birthday"] = birthday
            data["email"] = email
            data["age"] = age
            data["gender"] = gender

            val locationMap = HashMap<String, Any>()

            registerRequest.location.run {
                locationMap["lng"] = lng
                locationMap["lat"] = lat
                locationMap["city"] = city
                locationMap["country"] = country
                locationMap["lng"] = postalCode

            }
        }

        data["location"] = registerRequest.location
        data["job"] = ""
        data["university"] = ""
        data["description"] = ""
        data["surname"] = ""
        data["profileImageUrl"] = ""

        return data
    }

    fun userResponseMapper(response: Map<String, Any>): UserResponse {
        val userResponse = UserResponse()
        val location = Location()

      if(response.isNotEmpty()){
          response.let {
              userResponse.name = it["name"] as String
              userResponse.birthday = it["birthday"] as String
              userResponse.email = it["email"] as String
              userResponse.age = Integer.parseInt(it["age"].toString())
              userResponse.gender = User.Gender.valueOf(it["gender"].toString())
              userResponse.job = it["job"] as String
              userResponse.university = it["university"] as String
              userResponse.surname = it["surname"] as String
              userResponse.description = it["description"] as String
              userResponse.profileImageUrl = it["profileImageUrl"] as String



              if (response["location"] != null) {
                  val locationMap: HashMap<String, Any> = response["location"] as HashMap<String, Any>
                  if (locationMap.size > 0) {
                      location.lng = locationMap["lng"] as Double
                      location.lat = locationMap["lat"] as Double
                      location.postalCode = locationMap["postalCode"] as String
                      location.city = locationMap["city"] as String
                      location.country = locationMap["country"] as String
                  }
              }
              userResponse.location = location
          }
      }

        return userResponse
    }

    fun mapAllUsers(response: QuerySnapshot): ArrayList<UserResponse> {
        val users = ArrayList<UserResponse>()
        response.documents.forEach {
            if (it.data != null) {
                users.add(userResponseMapper(it.data!!))
            }


        }
        return users
    }


}