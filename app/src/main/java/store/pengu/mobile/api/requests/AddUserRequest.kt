package store.pengu.mobile.api.requests

data class AddUserRequest(
    val username: String = "",
    val email: String = "",
    val password: String = ""
)
