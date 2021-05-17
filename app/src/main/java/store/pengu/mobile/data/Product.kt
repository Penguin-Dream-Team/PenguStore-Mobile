package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class Product (
    val id: Long,
    val name: String,
    val barcode: String?,
    var productRating: Double,
    var userRating: Int,
    var ratings: List<Int>,
    val image: String?
) {
    fun toProductInPantry(listId: Long, amountAvailable: Int, amountNeeded: Int): ListProduct {
        return ProductInPantry(
            id = id,
            listId = listId,
            name = name,
            barcode = barcode,
            amountAvailable = amountAvailable,
            amountNeeded = amountNeeded,
            image = image
        )
    }
}