package com.example.justfivemins.api.requests


class RegisterRequest {
    var email: String = ""
    var password: String = ""
    var name: String = ""
    var confirmPassword: String = ""
    var type: LoginRequest.LoginType = LoginRequest.LoginType.NORMAL
    var birthday: String = ""

}