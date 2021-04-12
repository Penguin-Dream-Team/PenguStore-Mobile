package store.pengu.mobile.api.responses.account

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.google.gson.annotations.SerializedName

@JsonDeserialize
data class RegisterResponse(
    @SerializedName("password") val password: String,
    @SerializedName("token") val token: String,
    @SerializedName("refreshToken") val refreshToken: String,
)
