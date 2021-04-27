package store.pengu.mobile.api.requests

data class ImageRequest (
    val id: ULong?,
    val barcode: String?,
    val product_id: Long?,
    val image_url: String?
)
