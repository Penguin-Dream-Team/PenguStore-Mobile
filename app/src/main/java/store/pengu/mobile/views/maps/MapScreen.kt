package store.pengu.mobile.views.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import store.pengu.mobile.R
import store.pengu.mobile.states.StoreState
import javax.inject.Inject

@AndroidEntryPoint
class MapScreen : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var storeState: StoreState

    private var mapFrag: SupportMapFragment? = null
    private var selectedLocation: Location? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var locationRequest: LocationRequest
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
                Toast.makeText(applicationContext, "No location selected", Toast.LENGTH_SHORT)
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

        initMap()

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener { latLng ->
            selectLocation(pantryName, latLng)

            button.isEnabled = true
        }

        googleMap.setOnMyLocationButtonClickListener {
            return@setOnMyLocationButtonClickListener storeState.location?.run {
                selectLocation(pantryName, this)
                false
            } ?: true
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
    fun initMap() {
        googleMap.isMyLocationEnabled = false
        if (receivedLatLng != null) {
            selectLocation(pantryName, receivedLatLng!!, 15.0f)
        } else {
            storeState.location?.let {
                googleMap.isMyLocationEnabled = true
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15.0F))
            }
        }
    }
}