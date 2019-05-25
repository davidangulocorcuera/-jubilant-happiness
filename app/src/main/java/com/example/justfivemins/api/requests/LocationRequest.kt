package com.example.justfivemins.api.requests

import java.io.Serializable

class LocationRequest(var city: String = "", var country: String = ""
                      , var postalCode: String = "", var lat: Double = 2.1 , var lng: Double = 1.1) : Serializable {
}