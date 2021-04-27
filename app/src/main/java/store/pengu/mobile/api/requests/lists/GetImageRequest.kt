package store.pengu.mobile.api.requests.lists

data class GetImageRequest @ExperimentalUnsignedTypes constructor(
    val id: ULong,
    val barcode: String?,
    val product_id: Long?,
    val image_url: String
)
