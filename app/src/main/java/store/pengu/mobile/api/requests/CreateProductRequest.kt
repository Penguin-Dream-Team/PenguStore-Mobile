package store.pengu.mobile.api.requests

data class CreateProductRequest(
    val name: String,
    val barcode: String?,
    val image: String?
)
