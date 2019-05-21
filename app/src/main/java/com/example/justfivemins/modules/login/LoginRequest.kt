package com.example.justfivemins.modules.login

import com.example.justfivemins.modules.login.LoginRequest.LoginType.NORMAL


class LoginRequest() {
    var token: String? = null
    @Transient
    var type: LoginType = LoginType.NORMAL
    var email: String = ""
    var password: String = ""

    enum class LoginType {
        NORMAL,
        GOOGLE,
        FACEBOOK
    }

    constructor(
        token: String,
        type: LoginType = NORMAL
    ) : this() {
        this.token = token
        this.type = type
    }

}
