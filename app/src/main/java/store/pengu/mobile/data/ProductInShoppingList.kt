package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import store.pengu.mobile.data.productlists.ProductPantryListEntry

@JsonDeserialize
data class ProductInShoppingList (
    override val id: Long,
    override val listId: Long,
    override val name: String,
    override val barcode: String?,
    override val amountAvailable: Int,
    override val amountNeeded: Int,
    val price: Double,
    override val image: String?,
    val pantries: List<ProductPantryListEntry>
) : ListProduct(id, listId, name, barcode, amountAvailable, amountNeeded, image)