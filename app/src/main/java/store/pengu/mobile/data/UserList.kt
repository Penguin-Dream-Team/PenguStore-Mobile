package store.pengu.mobile.data

import com.google.android.gms.maps.model.LatLng

abstract class UserList(
    open val id: Long,
    open val name: String,
    open val code: String,
    open val latitude: Double,
    open val longitude: Double,
    open val color: String,
    open val shared: Boolean,
    open val productCount: Int
) {
    val location: LatLng get() = LatLng(latitude, longitude)
}
