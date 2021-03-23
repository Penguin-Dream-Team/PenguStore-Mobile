package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class Pantry(
    @SerializedName("name") val name: String,
    @SerializedName("privacy") val privacy: Boolean,
    @SerializedName("items") val items: List<Item>
)