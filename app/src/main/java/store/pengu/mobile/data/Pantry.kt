package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class Pantry(
    @SerializedName("id") val id: Long,
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String
)