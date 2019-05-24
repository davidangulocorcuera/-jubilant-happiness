package com.example.justfivemins.api.requests

import com.example.justfivemins.model.User


class RegisterRequest {
    var email: String = ""
    var password: String = ""
    var name: String = ""
    var confirmPassword: String = ""
    var type: LoginRequest.LoginType = LoginRequest.LoginType.NORMAL
    var birthday: String = ""
    var age: Int = 0
    var gender: User.Gender = User.Gender.OTHER

}