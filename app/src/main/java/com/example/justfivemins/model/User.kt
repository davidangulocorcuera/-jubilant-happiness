package com.example.justfivemins.model

import com.example.justfivemins.R
import java.io.Serializable

class User(
    var email: String = ""
    , var name: String = ""
    , var description: String = ""
    , var surname: String = ""
    , var age: Int = 27
    , var birthday: String = ""
    , var gender: Gender = Gender.OTHER
    , var phoneNumber: String = ""
    , var locations: ArrayList<Location> = ArrayList(),
      var profileImageUrl: String = ""
    , var images: ArrayList<Int> = ArrayList()
    , var friends: ArrayList<User> = ArrayList()
    , var numLikes: Int = 0
    , var universityName: String = ""
    , var jobName: String = "",
    var currentLocation: Location? = null
) : Serializable{
    enum class Gender {
       MALE,
        FEMALE,
        OTHER
    }
}