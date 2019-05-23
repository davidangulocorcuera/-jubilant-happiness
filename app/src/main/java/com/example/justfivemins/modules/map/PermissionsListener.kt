package com.example.justfivemins.modules.map

interface PermissionsListener {
    interface LocationListener{
        fun onLocationPermissionResponse(isAccepted: Boolean)
    }
}