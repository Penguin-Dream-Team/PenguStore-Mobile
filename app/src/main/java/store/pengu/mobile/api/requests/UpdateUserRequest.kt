package store.pengu.mobile.api.requests

data class UpdateUserRequest(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null
)
