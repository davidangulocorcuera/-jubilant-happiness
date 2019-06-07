package com.example.justfivemins.api.requests

import java.io.Serializable

    class LocationRequest(var city: String = "", var country: String = "", var countryCode: String = ""
                      , var postalCode: String = "", var lat: Double = 0.0 , var lng: Double =  0.0) : Serializable {
}