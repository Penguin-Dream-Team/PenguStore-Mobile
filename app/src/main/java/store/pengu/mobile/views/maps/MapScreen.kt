package store.pengu.mobile.views.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
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
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat

class MapScreen : AppCompatActivity(), OnMapReadyCallback, LocationListener  {
    lateinit var currentLocation : Location

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Check permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                101)
            return
        }

        //val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

        setContentView(R.layout.map_layout)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        //val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val latLng = LatLng(0.0, 0.0)
        googleMap?.apply {
            addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Current Location")
            )
            moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    override fun onLocationChanged(location: Location?) {
        currentLocation.latitude = location!!.latitude
        currentLocation.longitude = location.longitude
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}
    override fun onProviderDisabled(provider: String?) {}
}