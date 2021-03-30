package store.pengu.mobile.views.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import store.pengu.mobile.R

class MapFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.map_layout, container, false)

        val supportFragmentManager = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        supportFragmentManager.getMapAsync(OnMapReadyCallback() {
            fun onMapReady(googleMap: GoogleMap) {
                // When map is loaded
                googleMap.setOnMapClickListener(GoogleMap.OnMapClickListener() {
                    fun onMapClick(latLng: LatLng) {
                        // When clicked on map
                        // Initialize marker options
                        val markerOptions = MarkerOptions()
                        // Set position of marker
                        markerOptions.position(latLng)
                        // Set title of marker
                        markerOptions.title("${latLng.latitude} : ${latLng.longitude}")
                        // Remove all marker
                        googleMap.clear()
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            latLng, 10F
                        ))
                        // Add marker on map
                        googleMap.addMarker(markerOptions)
                    }
                })
            }
        })

        return view
    }
}