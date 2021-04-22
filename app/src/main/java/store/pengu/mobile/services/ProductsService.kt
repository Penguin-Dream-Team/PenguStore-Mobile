package store.pengu.mobile.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.states.StoreState

class ProductsService(
    private val api: PenguStoreApi,
    private val store: StoreState
) {

    fun getProducts() = GlobalScope.launch(Dispatchers.Main) {
        store.products.clear()
        store.products.addAll(api.products().data)
    }

    fun getPantryProducts(pantryId: Long) = GlobalScope.launch(Dispatchers.Main) {
        store.pantryProducts.clear()
        store.pantryProducts.addAll(api.getPantryProducts(pantryId).data)
    }

    fun getShoppingListProducts(userId: Long) = GlobalScope.launch(Dispatchers.Main) {
        store.shoppingListProducts.clear()
        store.shoppingListProducts.addAll(api.getUserShoppingListProducts(userId).data)
    }

    fun addProduct(
        pantryId: Long,
        productId: Long,
        amountAvailable: Int,
        amountNeeded: Int
    ) = GlobalScope.launch(Dispatchers.Main) {
        api.addPantryProduct(pantryId, productId, amountAvailable, amountNeeded)
    }

    fun updateProduct(
        pantryId: Long,
        productId: Long,
        amountAvailable: Int,
        amountNeeded: Int
    ) = GlobalScope.launch(Dispatchers.Main) {
        api.updatePantryProduct(pantryId, productId, amountAvailable, amountNeeded)
        getProducts()
    }

    fun addBarcodeProduct(barcode: String) = GlobalScope.launch(Dispatchers.Main) {
        api.addBarcodeProduct(barcode)
    }

    fun deleteProduct(
        pantryId: Long,
        productId: Long
    ) = GlobalScope.launch(Dispatchers.Main) {
        api.deletePantryProduct(pantryId, productId)
        getProducts()
    }
}