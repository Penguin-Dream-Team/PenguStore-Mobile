package store.pengu.mobile.api.requests.products

data class AddProductToPantryListRequest(
    val pantryId: Long,
    val haveQuantity: Int,
    val needQuantity: Int
)
