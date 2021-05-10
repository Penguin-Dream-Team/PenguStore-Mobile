package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
abstract class ListProduct(
    open val id: Long,
    open val listId: Long,
    open val name: String,
    open val barcode: String?,
    open val amountAvailable: Int,
    open val amountNeeded: Int,
    open val image: String?
) {
    fun toProduct(): Product {
        return Product(
            id = id,
            name = name,
            barcode = barcode,
            productRating = 0f,
            userRating = 0,
            ratings = emptyList(),
            image = image
        )
    }
}