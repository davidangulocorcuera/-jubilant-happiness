package com.example.justfivemins.model

import android.R
import com.google.android.gms.maps.model.MarkerOptions

class UserMarker(
    override var title: String,
    override var description: String,
    override var image: Int,
    override var markerOptions: MarkerOptions,
    override var markerId: R.string
) : MarkerItem {
}