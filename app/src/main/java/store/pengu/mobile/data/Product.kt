package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class Product (
    @SerializedName("productId") val productId: Long,
    @SerializedName("pantryId") val pantryId: Long?,
    @SerializedName("name") val name: String,
    @SerializedName("barcode") val barcode: String,
    @SerializedName("reviewScore") val reviewScore: Double?,
    @SerializedName("reviewNumber") val reviewNumber: Int,
    @SerializedName("amountAvailable") val amountAvailable: Int?,
    @SerializedName("amountNeeded") val amountNeeded: Int?,
)