package com.example.mjivan.weatherforcasterv1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.Manifest
import android.widget.Toast
import android.content.pm.PackageManager
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLngBounds

//import javax.naming.Context

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    //adding permission request code
    private val LOCATION_REQUEST_CODE=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val zoomLevel = 16.0f //This goes up to 21


        if(mMap !=null) {
           // val CoarseLocationPermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)


            if (permission == PackageManager.PERMISSION_GRANTED)
            {
                mMap?.isMyLocationEnabled = true;
            }

            else
            {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        LOCATION_REQUEST_CODE)

            }

        }
        //Requesting App Permission

    }
    private fun requestPermission(permissionType:String,requestCode:Int)
    {
        ActivityCompat.requestPermissions(this,arrayOf(permissionType),
        requestCode)
    }
//Permission Handling
  override fun onRequestPermissionsResult(requestCode:Int,permissions:Array<String>,grantResults:IntArray)
    {
        when(requestCode)
        {
            LOCATION_REQUEST_CODE ->
            {
             if(grantResults.isEmpty()|| grantResults[0]!=
                     PackageManager.PERMISSION_GRANTED)
             {
                 Toast.makeText(this,
                         "Sorry,we cannot display your location,please grant us permissions to do so :)",
                         Toast.LENGTH_LONG).show()
             }

                else
             {
                 val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                 mapFragment.getMapAsync(this)
             }
            }
        }
    }




}
