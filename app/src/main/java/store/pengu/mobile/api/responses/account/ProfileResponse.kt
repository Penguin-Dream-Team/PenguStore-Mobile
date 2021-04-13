package store.pengu.mobile.api.responses.account

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class ProfileResponse(
    val username: String,
    val email: String?,
    val guest: Boolean,
)
