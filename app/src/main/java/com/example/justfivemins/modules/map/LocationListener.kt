package com.example.justfivemins.modules.map

import com.google.android.gms.location.LocationResult

interface LocationListener {
    fun locationResponse(locationResult: LocationResult)
}