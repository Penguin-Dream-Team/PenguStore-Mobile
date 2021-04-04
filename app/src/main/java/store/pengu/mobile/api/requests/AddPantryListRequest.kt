package store.pengu.mobile.api.requests

data class AddPantryListRequest(
    val userId: Long = -1,
    val code: String = "abc",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
