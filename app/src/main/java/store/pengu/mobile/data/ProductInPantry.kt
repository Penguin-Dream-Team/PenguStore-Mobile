package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class ProductInPantry (
    override val id: Long,
    override val listId: Long,
    override val name: String,
    override val barcode: String?,
    override val amountAvailable: Int,
    override val amountNeeded: Int,
    override val image: String?
) : ListProduct(id, listId, name, barcode, amountAvailable, amountNeeded, image)
