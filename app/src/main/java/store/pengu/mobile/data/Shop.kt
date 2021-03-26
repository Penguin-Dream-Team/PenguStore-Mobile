package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class Shop(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("locationX") val locationX: Float,
    @SerializedName("locationY") val locationY: Float
)