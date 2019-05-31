package com.example.justfivemins.api.requests

import java.io.Serializable

    class LocationRequest(var city: String = "Miami", var country: String = "United States"
                      , var postalCode: String = "", var lat: Double = 25.761681 , var lng: Double =  -80.191788) : Serializable {
}