package store.pengu.mobile.services

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.data.ProductInShoppingList
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

    fun getShoppingListProducts(shoppingListId: Long) = GlobalScope.launch(Dispatchers.Main) {
        store.shoppingListProducts.clear()
        store.shoppingListProducts.addAll(api.getUserShoppingList(shoppingListId).data)
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

    suspend fun timeQueue(): Int {
        store.location = LatLng(50.25, 150.25)
        return api.timeQueue(store.location!!).data
    }

    suspend fun addProduct(imageId: Int, imageUrl: String): String {
        val product = store.selectedProduct!!

        return api.addProductImage(imageId, product.barcode, product.id, imageUrl).data
    }

    suspend fun deleteProduct(imageId: Int, imageUrl: String): String {
        val product = store.selectedProduct!!

        return api.deleteProductImage(imageId, product.barcode, product.id, imageUrl).data
    }

    suspend fun getProductImages(): List<String> {
        return if (store.selectedProduct!!.barcode != null)
            api.getProductImageBarcode(store.selectedProduct!!.barcode!!).data
        else
            api.getProductImageProductId(store.selectedProduct!!.id).data
    }
}