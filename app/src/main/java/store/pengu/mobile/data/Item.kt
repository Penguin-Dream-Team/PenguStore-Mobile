package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class Item (
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("amountAvailable") val amountAvailable: Int,
    @SerializedName("amountNeeded") var amountNeeded: Int
)