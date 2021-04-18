package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class PantryList(
    override val id: Long,
    val code: String,
    override val name: String,
    override val latitude: Float,
    override val longitude: Float,
    val productCount: Int,
    val isShared: Boolean = false
) : UserList(id, name, latitude, longitude)
