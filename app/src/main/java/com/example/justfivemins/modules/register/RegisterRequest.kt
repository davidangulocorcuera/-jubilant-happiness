package com.example.justfivemins.modules.register

import com.example.justfivemins.modules.login.LoginRequest

class RegisterRequest {
    enum class Gender {
        Male,
        Female,
        Not
    }


    var privacy = false
    var email: String? = null
    var password: String? = null
    var name: String? = null
    var surname: String? = null
    var phone: String? = null
    var birthdate: String? = null
    var identification: String? = null
    var confirmPassword: String? = null
    var type: LoginRequest.LoginType = LoginRequest.LoginType.NORMAL
    var gender: String? = Gender.Not.toString()
    var token: String? = null
}