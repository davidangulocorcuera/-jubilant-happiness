package com.example.justfivemins.api.responses

import com.example.justfivemins.model.Location
import com.example.justfivemins.model.User
import java.io.Serializable

class UserResponse (var name: String = "",
                    var email: String = "",
                    var location: Location? = null,
                    var birthday: String = "",
                    var age: Int = 0,
                    var gender: User.Gender = User.Gender.OTHER,
                    var surname: String = "",
                    var description: String = "",
                    var university: String = "",
                    var job: String = ""
): Serializable