package store.pengu.mobile.api.requests.lists

data class CreateListRequest(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val color: String
)
