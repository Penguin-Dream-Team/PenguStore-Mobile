package store.pengu.mobile.api.requests

data class UpdatePantryProductRequest(
    val pantryId: Long = -1,
    val productId: Long = -1,
    val amountAvailable: Int = -1,
    val amountNeeded: Int = -1
)
