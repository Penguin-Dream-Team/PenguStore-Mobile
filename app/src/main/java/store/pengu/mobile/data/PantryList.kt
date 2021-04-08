package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class PantryList(
    @SerializedName("id") val id: Long,
    @SerializedName("name") override val name: String,
    @SerializedName("code") val code: String,
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
    @SerializedName("productsCount") val productsCount: Int
) : ListTypes