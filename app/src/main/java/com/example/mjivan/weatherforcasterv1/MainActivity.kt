package com.example.mjivan.weatherforcasterv1

import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.example.mjivan.weatherforcasterv1.Common.Common
import com.example.mjivan.weatherforcasterv1.Common.Helper
import com.example.mjivan.weatherforcasterv1.Model.OpenWeatherMap
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{



    val PERMISSION_REQUEST_CODE=101
    val PLAY_SERVICE_RESOLUTION_REQUEST=100

    //Variables
    var mGoogleApiClient:GoogleApiClient?=null
    var mLocationRequest:LocationRequest?=null
    internal var openWeatherMap = OpenWeatherMap()

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        requestPermission()
if(checkPlayService())
    buildGoogleApiClient()

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
                    if (checkPlayService())
                    {
                        buildGoogleApiClient()
                    }

                }

            }
        }
    }


    private fun buildGoogleApiClient()
    {
mGoogleApiClient = GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API).build()
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

    override fun onMapReady(p0: GoogleMap?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
       createLocationRequest();
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.fastestInterval=5000
        mLocationRequest!!.priority=LocationRequest.PRIORITY_HIGH_ACCURACY

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            return
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this)
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
       Log.i("ERROR","Connection failed: "+p0.errorCode)
    }

    override fun onLocationChanged(location: Location?) {
       GetWeather().execute(Common.apiRequest(location!!.latitude.toString(),location!!.longitude.toString()))
        
    }


    override fun onStart() {
        super.onStart()
        if(mGoogleApiClient !=null)
            mGoogleApiClient!!.connect()

    }

    override fun onDestroy() {
        mGoogleApiClient!!.disconnect()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        checkPlayService()
    }


    private inner class GetWeather: AsyncTask<String, Void, String>()
    {
        internal var pd = ProgressDialog(this@MainActivity)

        override fun onPreExecute()
        {
            super.onPreExecute()

            pd.setTitle("Please wait")
            pd.show()
        }
        override fun doInBackground(vararg params: String?): String
        {
           var stream:String?=null
            var urlString=params[0]

            val http = Helper()
            stream = http.getHTTPData(urlString)
            return stream
        }

        override fun onPostExecute(result:String?)
        {
            super.onPostExecute(result)
            if(result!!.contains("Error:City not found"))
            {
                pd.dismiss()
                return
            }

            val gson=Gson()
            val mType = object:TypeToken<OpenWeatherMap> (){}.type
            openWeatherMap = gson.fromJson<OpenWeatherMap>(result,mType)
            pd.dismiss()

            //Set info to UI
            txtCity.text = "${openWeatherMap.sys!!.country}"
            txtLastUpdate.text="Last Updated:  ${Common.dateNow}"
            txtDescription.text="${openWeatherMap.weather!![0].description}"
            txtHumidity.text="${openWeatherMap.main!!.humidity}"
            txtCelcius.text= "${openWeatherMap.main!!.temp} C"
            txtTime.text="${Common.unixTimeSDtampToDateTime(openWeatherMap.sys!!.sunrise)}/${Common.unixTimeSDtampToDateTime(openWeatherMap.sys!!.sunset)}\""
            Picasso.with(this@MainActivity)
                    .load(Common.getImage(openWeatherMap.weather!![0].icon!!))
                    .into(imageView)
            
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





    }


}
