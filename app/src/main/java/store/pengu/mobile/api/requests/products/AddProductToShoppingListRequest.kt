package store.pengu.mobile.api.requests.products

data class AddProductToShoppingListRequest(
    val shoppingListId: Long,
    val price: Double,
)
