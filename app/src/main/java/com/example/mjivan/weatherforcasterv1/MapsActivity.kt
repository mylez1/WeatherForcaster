package com.example.mjivan.weatherforcasterv1

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
val PERMISSION_REQUEST_CODE=101
    val PLAY_SERVICE_RESOLUTION_REQUEST=100

    //Variables
    val mGoogleApiClient:GoogleApiClient?=null
    val mLocationRequest:LocationRequest?=null


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        requestPermission()
    }

    private fun requestPermission() {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_REQUEST_CODE)
                }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayService()) {
                        buildGoogleApiClient()
                    }

                }

            }
        }
    }


    private fun buildGoogleApiClient()
    {
mGoogleApiClient = GoogleApiClient.Builder(this)
        .addConnectionCallbacks()
    }
    private fun checkPlayService(): Boolean
    {
        var resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICE_RESOLUTION_REQUEST).show()
            }

            else
            {
                Toast.makeText(applicationContext,"This device is not supported", Toast.LENGTH_SHORT).show()
                finish()
            }
            return false
        }
    return true
        }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //override fun onMapReady(googleMap: GoogleMap) {
       // mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
    //    mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    //    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

     //   googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15F))



fun checkPermission(context: Context, permissionArray :Array<String>):Boolean
{
    return false
}

    }


}
