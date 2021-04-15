package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class ShoppingList2 (
    val shopId: Long,
    val userId: Long,
    val name: String
)