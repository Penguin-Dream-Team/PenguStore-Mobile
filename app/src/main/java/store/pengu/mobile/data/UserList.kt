package store.pengu.mobile.data

abstract class UserList(
    open val id: Long,
    open val name: String,
    open val latitude: Float,
    open val longitude: Float
)
