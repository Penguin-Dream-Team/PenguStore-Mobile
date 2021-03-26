package store.pengu.mobile.api.requests

data class AddUserPantryRequest(
    val userId: Long = -1,
    val pantryCode: String = ""
)
