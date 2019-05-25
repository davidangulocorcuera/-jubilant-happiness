package com.example.justfivemins.api.requests

import java.io.Serializable

class UpdateUserRequest: Serializable{
    var name: String = ""
    var surname: String = ""
    var description: String = ""
    var university: String = ""
    var job: String = ""
}
