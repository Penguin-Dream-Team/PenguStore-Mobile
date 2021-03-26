package store.pengu.mobile.api.requests

data class AddPantryRequest(
    val pantryId: Long = -1,
    val pantryCode: String = "",
    val pantryName: String = ""
)
