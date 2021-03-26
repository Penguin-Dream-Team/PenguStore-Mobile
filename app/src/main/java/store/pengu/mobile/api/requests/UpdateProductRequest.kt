package store.pengu.mobile.api.requests

data class UpdateProductRequest(
    val productId: Long = -1,
    val productName: String = "",
    val productBarcode: String = "",
    val productReviewScore: Double = 0.0,
    val productReviewNumber: Int = -1
)
