package com.example.justfivemins.modules.location



import android.view.View
import com.example.justfivemins.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.example.justfivemins.modules.base.BaseFragment



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
