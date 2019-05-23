package com.example.justfivemins.modules.map



import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.justfivemins.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.widget.Toast
import com.example.justfivemins.modules.base.BaseActivity

import com.example.justfivemins.modules.base.BaseFragment
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : BaseFragment(), OnMapReadyCallback {
    override fun onMapReady(p0: GoogleMap?) {

    }

    private lateinit var mMap: GoogleMap

    override fun onCreateViewId(): Int {
        return R.layout.fragment_map
    }

    override fun viewCreated(view: View?) {



    }

}
