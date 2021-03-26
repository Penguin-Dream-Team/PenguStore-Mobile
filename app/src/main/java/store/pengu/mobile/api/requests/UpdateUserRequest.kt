package store.pengu.mobile.api.requests

data class UpdateUserRequest(
    val userId: Long = -1,
    val username: String = "",
    val email: String = "",
    val password: String = ""
)
