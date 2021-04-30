package store.pengu.mobile.api.responses.account

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.google.gson.annotations.SerializedName
import store.pengu.mobile.api.Response

@JsonDeserialize
data class LoginResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("token") val token: String,
)
