package store.pengu.mobile.api.requests

data class UpdatePantryRequest(
    val pantryId: Long = -1,
    val pantryCode: String = "",
    val pantryName: String = ""
)
