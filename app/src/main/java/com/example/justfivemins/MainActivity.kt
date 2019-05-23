package com.example.justfivemins


import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.WindowManager
import com.example.justfivemins.modules.base.BaseActivity
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.map.LocationFragment
import com.example.justfivemins.modules.map.PermissionsListener


class MainActivity(
    var fragment: BaseFragment? = null
) : BaseActivity() {
    private val REQUEST_CODE_LOCATION = 100
    private val permissionFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permissionCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private var locationListener: PermissionsListener.LocationListener? = LocationFragment()

    override fun onCreateViewId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.navigateToLogin()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun validatePermissionsLocation(): Boolean {
        val fineLocationAvailable = fragment?.context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                permissionFineLocation
            )
        } == PackageManager.PERMISSION_GRANTED
        val coarseLocationAvailable = fragment?.context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                permissionCoarseLocation
            )
        } == PackageManager.PERMISSION_GRANTED
        return fineLocationAvailable && coarseLocationAvailable
    }

    fun permissionRequest() {
        fragment?.activity?.let {
            ActivityCompat.requestPermissions(
                it, arrayOf(
                    permissionFineLocation,
                    permissionCoarseLocation
                ), REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationListener?.onLocationPermissionResponse(true)
                } else {
                    locationListener?.onLocationPermissionResponse(false)

                }
            }
        }
    }


}
