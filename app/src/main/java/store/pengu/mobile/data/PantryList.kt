package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class PantryList(
    @SerializedName("name") override val name: String,
    @SerializedName("id") val id: Long,
    @SerializedName("code") val code: String
) : ListTypes