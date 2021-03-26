package store.pengu.mobile.api.requests

data class DeleteUserPantryRequest(
    val userId: Long = -1,
    val pantryCode: String = ""
)
