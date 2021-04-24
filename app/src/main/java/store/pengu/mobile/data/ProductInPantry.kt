package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class ProductInPantry (
    val productId: Long,
    val pantryId: Long,
    val name: String,
    val barcode: String?,
    val amountAvailable: Int,
    val amountNeeded: Int
)