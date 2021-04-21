package store.pengu.mobile.utils

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.lang.StringBuilder


object GeoUtils {
    fun getLocationName(context: Context, latLng: LatLng): String {
        return getLocationName(context, latLng.latitude, latLng.longitude)
    }

    fun getLocationName(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context)
        return geocoder.getFromLocation(latitude, longitude,1).firstOrNull()?.run {
            val sb = StringBuilder()
            if (thoroughfare != null) {
                sb.append(thoroughfare, ", ")
            }
            if (locality != null) {
                sb.append(locality, ", ")
            }
            if (countryName != null) {
                sb.append(countryName)
            }
            sb.toString()
        } ?: ""
    }
}