package store.pengu.mobile.api.requests

data class LeaveQueueRequest(
    val latitude: Double,
    val longitude: Double,
    val num_items: Int,
    val time: Int
)
