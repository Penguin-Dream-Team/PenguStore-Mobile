package store.pengu.mobile.api.requests

data class AddShoppingListRequest(
    val shopId: Long = -1,
    val userId: Long = -1,
    val name: String = ""
)
