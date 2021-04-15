package store.pengu.mobile.api.responses.lists

data class UserListResponse(
    val type: UserListType,
    val list: LinkedHashMap<String, Any>
)
