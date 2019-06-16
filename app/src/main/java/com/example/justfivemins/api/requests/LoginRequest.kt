package com.example.justfivemins.api.requests

import com.example.justfivemins.api.requests.LoginRequest.LoginType.NORMAL
import java.io.Serializable


class LoginRequest(): Serializable {
    var token: String? = null
    @Transient
    var type: LoginType = LoginType.NORMAL
    var email: String = ""
    var password: String = ""

    /** need to be implemented */
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
