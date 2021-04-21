package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class ShoppingList(
    override val id: Long,
    override val name: String,
    override val latitude: Double,
    override val longitude: Double,
    override val color: String,
    override val shared: Boolean
) : UserList(id, name, latitude, longitude, color, shared)
