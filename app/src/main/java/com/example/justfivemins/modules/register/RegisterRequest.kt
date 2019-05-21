package com.example.justfivemins.modules.register

import com.example.justfivemins.modules.login.LoginRequest


class RegisterRequest {
    var email: String = ""
    var password: String = ""
    var name: String = ""
    var surname: String = ""
    var confirmPassword: String = ""
    var type: LoginRequest.LoginType = LoginRequest.LoginType.NORMAL

}