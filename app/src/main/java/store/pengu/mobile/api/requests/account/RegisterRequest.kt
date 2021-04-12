package store.pengu.mobile.api.requests.account

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.google.gson.annotations.SerializedName

@JsonSerialize
data class RegisterRequest(
    @SerializedName("username") val username: String
)
