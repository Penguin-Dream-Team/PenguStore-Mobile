package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class PantryList(
    override val id: Long,
    override val name: String,
    override val code: String,
    override val latitude: Double,
    override val longitude: Double,
    val productCount: Int,
    override val color: String,
    override val shared: Boolean,
) : UserList(id, name, code, latitude, longitude, color, shared)
