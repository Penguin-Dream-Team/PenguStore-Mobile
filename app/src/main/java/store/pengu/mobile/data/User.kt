package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userId") val userId: Long,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String
)
