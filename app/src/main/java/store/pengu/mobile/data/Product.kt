package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class Product (
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("barcode") val barcode: String,
    @SerializedName("reviewScore") val reviewScore: Double,
    @SerializedName("reviewNumber") val reviewNumber: Int
)