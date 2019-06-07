package com.example.justfivemins.api.requests

import com.example.justfivemins.model.Location
import com.example.justfivemins.model.User
import java.io.Serializable


class RegisterRequest: Serializable {
    var email: String = ""
    var id: String = ""
    var password: String = ""
    var name: String = ""
    var confirmPassword: String = ""
    var type: LoginRequest.LoginType = LoginRequest.LoginType.NORMAL
    var birthday: String = ""
    var age: Int = 0
    var gender: User.Gender = User.Gender.OTHER
    var location: Location = Location()


}