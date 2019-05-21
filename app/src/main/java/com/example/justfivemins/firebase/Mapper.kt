package com.example.justfivemins.firebase

import com.example.justfivemins.modules.register.RegisterRequest



object Mapper {
    fun registerRequestMapper(registerRequest: RegisterRequest) : Map<String, Any> {
        val data = HashMap<String, Any>()
        data["name"] = registerRequest.name
        data["surname"] = registerRequest.surname
        data["email"] = registerRequest.email
        return data
    }
}