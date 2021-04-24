package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class ProductInShoppingList (
    val product_id: Long,
    val pantry_id: Long,
    val name: String,
    val barcode: String?,
    val amountAvailable: Int,
    val amountNeeded: Int,
    val price: Double
)