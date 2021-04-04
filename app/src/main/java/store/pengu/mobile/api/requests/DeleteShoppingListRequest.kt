package store.pengu.mobile.api.requests

data class DeleteShoppingListRequest(
    val shopId: Long = -1,
    val userId: Long = -1,
    val name: String = ""
)
