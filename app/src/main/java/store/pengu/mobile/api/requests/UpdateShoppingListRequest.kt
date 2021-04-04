package store.pengu.mobile.api.requests

data class UpdateShoppingListRequest(
    val shopId: Long = -1,
    val userId: Long = -1,
    val name: String = ""
)
