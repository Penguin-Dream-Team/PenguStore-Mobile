package store.pengu.mobile.api.requests

data class DeleteShopProductRequest(
    val shopId: Long = -1,
    val productId: Long = -1,
    val price: Double = 0.0
)
