package store.pengu.mobile.views.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import store.pengu.mobile.R

class MapScreen : AppCompatActivity(), OnMapReadyCallback {

    private var mapFrag: SupportMapFragment? = null
    private var lastLocation: Location? = null
    private var selectedLocation: Location? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var locationRequest: LocationRequest
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var button: Button
    private var receivedLatLng: LatLng? = null
    private var pantryName: String = ""

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        button.isEnabled = selectedLocation != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)

        supportActionBar?.title = "Google Maps"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)

        pantryName = intent.getStringExtra("NAME") ?: ""
        val hasArgs = intent.getBooleanExtra("HAS_LOCATION", false)
        val lat = intent.getDoubleExtra("LATITUDE", 0.0)
        val lng = intent.getDoubleExtra("LONGITUDE", 0.0)
        if (hasArgs) {
            receivedLatLng = LatLng(lat, lng)
        }

        button = findViewById(R.id.confirmButton)
        button.isEnabled = false
        button.setOnClickListener {
            if (selectedLocation == null) {
                button.isEnabled = false
                Toast.makeText(applicationContext, getString(R.string.no_location_selected), Toast.LENGTH_SHORT)
                    .show()
            } else {
                setResult(
                    RESULT_OK, Intent()
                        .putExtra("LATITUDE", selectedLocation!!.latitude)
                        .putExtra("LONGITUDE", selectedLocation!!.longitude)
                )
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        locationRequest = LocationRequest.create().apply {
            interval = 120000 // two minute interval
            fastestInterval = 120000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        fusedLocationClient?.removeLocationUpdates(this)
                    }
                },
                Looper.getMainLooper()
            )
        } else {
            //Request Location Permission
            checkLocationPermission()
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initMap()
        } else {
            //Request Location Permission
            checkLocationPermission()
        }

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener { latLng ->
            selectLocation(pantryName, latLng)

            button.isEnabled = true
        }

        googleMap.setOnMyLocationButtonClickListener {
            if (lastLocation != null) {
                val latLng = LatLng(lastLocation!!.latitude, lastLocation!!.longitude)
                selectLocation(pantryName, latLng)
                false
            } else {
                true
            }
        }
    }

    private fun selectLocation(title: String, latLng: LatLng, zoom: Float? = null) {
        selectedLocation = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = latLng.latitude
            longitude = latLng.longitude
        }

        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title(title)

        // Clears the previously touched position
        googleMap.clear()
        // Animating to the touched position
        if (zoom != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        } else {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
        // Placing a marker on the touched position
        googleMap.addMarker(markerOptions)
        button.isEnabled = true
    }

    @SuppressLint("MissingPermission")
    private fun initMap() {
        googleMap.isMyLocationEnabled = false
        if (receivedLatLng != null) {
            selectLocation(pantryName, receivedLatLng!!, 15.0f)
        } else {
            //Location Permission already granted
            fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                if (location != null) {
                    lastLocation = location
                    googleMap.isMyLocationEnabled = true
                    //Place current location marker
                    val latLng = LatLng(location.latitude, location.longitude)
                    //move map camera
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0F))
                }
            }
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
                    .setTitle(getString(R.string.location_permition_needed))
                    .setMessage(getString(R.string.location_permission_needed_text))
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
                        initMap()
                    }
                } else {
                    // permission was denied | Disable the location-related task
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}