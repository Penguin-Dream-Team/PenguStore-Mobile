package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class CartProduct (
    val product_id: Long,
    val pantry_id: Long,
    val amount: Int
)