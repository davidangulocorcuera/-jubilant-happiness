package com.example.justfivemins.api.responses

import com.example.justfivemins.model.Location
import com.example.justfivemins.model.User

class UserResponse (var name: String = "",  var email: String = "", var location: Location? = null,var birthday: String = "", var age: Int = 0,var gender: User.Gender = User.Gender.OTHER)