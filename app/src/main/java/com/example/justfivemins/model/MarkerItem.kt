package com.example.justfivemins.model

import android.R.string
import android.os.Parcelable
import com.google.android.gms.maps.model.MarkerOptions



interface MarkerItem{
    var title: String
    var description: String
    var image: Int
    var markerOptions: MarkerOptions
    var markerId: string
}