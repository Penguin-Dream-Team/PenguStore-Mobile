package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class CartProduct (
    val productId: Long,
    val pantryId: Long,
    val amount: Int
)