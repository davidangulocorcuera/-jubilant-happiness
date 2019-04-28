package com.example.justfivemins.modules.map



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
import com.example.justfivemins.modules.base.BaseActivity

import com.example.justfivemins.modules.base.BaseFragment
import com.google.android.gms.location.LocationResult


class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var location: Location?=null

    override fun onCreateViewId(): Int {
        return R.layout.fragment_map
    }

    override fun viewCreated(view: View?) {

        setToolbarTitle(getString(R.string.map_title))

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        location = Location(this.activity as BaseActivity, object : LocationListener{
            override fun locationResponse(locationResult: LocationResult) {
                mMap.clear()
                val userLocation = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                mMap.addMarker(MarkerOptions().position(userLocation).title("Hi").snippet("Let's go!"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
            }
        })

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        location?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onStart() {
        super.onStart()
        location?.inicializeLocation()
    }
    override fun onPause() {
        super.onPause()
        location?.stopUpdateLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        onStart()

    }
    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}
