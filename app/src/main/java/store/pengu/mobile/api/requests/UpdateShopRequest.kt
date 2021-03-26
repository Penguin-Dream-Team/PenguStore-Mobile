package store.pengu.mobile.api.requests

data class UpdateShopRequest(
    val shopId: Long = -1,
    val shopName: String = "",
    val shopLocationX: Float = 0.0f,
    val shopLocationY: Float = 0.0f
)
