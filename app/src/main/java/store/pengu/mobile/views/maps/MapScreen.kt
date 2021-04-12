package store.pengu.mobile.views.maps

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import store.pengu.mobile.R
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import android.content.Intent
import store.pengu.mobile.views.MainActivity

class MapScreen : AppCompatActivity(), OnMapReadyCallback {

    var mapFrag: SupportMapFragment? = null
    var lastLocation: Location? = null
    var selectedLocation: Location? = null
    lateinit var googleMap: GoogleMap
    lateinit var locationRequest: LocationRequest
    internal var currentLocationMarker: Marker? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()
                Log.i("MapScreen", "Location: ${location.latitude} ${location.longitude}")
                lastLocation = location
                if (currentLocationMarker != null) {
                    currentLocationMarker?.remove()
                }

                //Place current location marker
                val latLng = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title("Current Position")
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                currentLocationMarker = googleMap.addMarker(markerOptions)

                //move map camera
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0F))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)

        supportActionBar?.title = "Google Maps"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)

        val button = findViewById<Button>(R.id.confirmButton)
        button.setOnClickListener {
            setResult(RESULT_OK, Intent()
                .putExtra("LATITUDE", selectedLocation!!.latitude)
                .putExtra("LONGITUDE", selectedLocation!!.longitude))
            finish()
        }
    }

    public override fun onPause() {
        super.onPause()

        //stop location updates when Screen is no longer active
        fusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        locationRequest = LocationRequest()
        locationRequest.interval = 120000 // two minute interval
        locationRequest.fastestInterval = 120000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //Location Permission already granted
            fusedLocationClient?.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper()!!)
            this.googleMap.isMyLocationEnabled = true
        } else {
            //Request Location Permission
            checkLocationPermission()
        }

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener { latLng ->
            selectedLocation = Location(LocationManager.GPS_PROVIDER).apply {
                latitude = latLng.latitude
                longitude = latLng.longitude
            }

            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

            // Clears the previously touched position
            googleMap.clear()
            // Animating to the touched position
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions)
        }

        googleMap.setOnMyLocationButtonClickListener {
            val latLng = LatLng(lastLocation!!.latitude, lastLocation!!.longitude)

            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("Current Position")

            // Clears the previously touched position
            googleMap.clear()
            // Animating to the touched position
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions)

            true
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this@MapScreen,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted | Do the location-related task
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        fusedLocationClient?.requestLocationUpdates(
                            locationRequest,
                            mLocationCallback,
                            Looper.myLooper()!!
                        )
                        googleMap.isMyLocationEnabled = true
                    }

                } else {

                    // permission was denied | Disable the location-related task
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}