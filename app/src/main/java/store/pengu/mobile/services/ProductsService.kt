package store.pengu.mobile.services

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.data.ListProduct
import store.pengu.mobile.data.Product
import store.pengu.mobile.data.ProductInPantry
import store.pengu.mobile.data.ProductInShoppingList
import store.pengu.mobile.states.StoreState

class ProductsService(
    private val api: PenguStoreApi,
    private val store: StoreState
) {
    private val pantryProducts = mutableMapOf<Long, SnapshotStateList<ProductInPantry>>()
    private val shoppingProducts = mutableMapOf<Long, SnapshotStateList<ProductInShoppingList>>()
    private val allProducts = mutableStateListOf<Product>()

    fun getAllProducts(): SnapshotStateList<Product> {
        return allProducts
    }

    fun getPantryProducts(pantryId: Long): SnapshotStateList<ProductInPantry> {
        if (!pantryProducts.contains(pantryId)) {
            pantryProducts[pantryId] = mutableStateListOf()
        }
        return pantryProducts[pantryId]!!
    }

    fun getShoppingListProducts(shoppingListId: Long): SnapshotStateList<ProductInShoppingList> {
        if (!shoppingProducts.contains(shoppingListId)) {
            shoppingProducts[shoppingListId] = mutableStateListOf()
        }
        return shoppingProducts[shoppingListId]!!
    }

    suspend fun fetchAllProducts() {
        try {
            val received = api.getAllProducts().data
            allProducts.removeIf { old ->
                received.find { new ->
                    old.id == new.id
                } == null
            }
            allProducts.replaceAll { old ->
                received.find { new ->
                    old.id == new.id
                }!!
            }
            allProducts.addAll(received.filterNot { new ->
                allProducts.contains(new)
            })
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    suspend fun fetchPantryProducts(pantryId: Long) {
        try {
            val received = api.getPantryProducts(pantryId).data
            if (!pantryProducts.contains(pantryId)) {
                pantryProducts[pantryId] = mutableStateListOf()
            }
            updateList(received, pantryProducts[pantryId]!!)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    suspend fun fetchShoppingListProducts(shoppingListId: Long) {
        try {
            val received = api.getShoppingListProducts(shoppingListId).data
            if (!shoppingProducts.contains(shoppingListId)) {
                shoppingProducts[shoppingListId] = mutableStateListOf()
            }
            updateList(received, shoppingProducts[shoppingListId]!!)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    private fun <T : ListProduct> updateList(
        received: List<T>,
        listToUpdate: SnapshotStateList<T>
    ) {
        listToUpdate.removeIf { old ->
            received.find { new ->
                old.id == new.id
            } == null
        }
        listToUpdate.replaceAll { old ->
            received.find { new ->
                old.id == new.id
            }!!
        }
        listToUpdate.addAll(received.filterNot { new ->
            listToUpdate.contains(new)
        })
    }

    suspend fun createProduct(name: String, barcode: String?, image: String?) {
        api.createProduct(name, barcode, image)
    }

    /**
     * NO
     */

    fun addBarcodeProduct(barcode: String) = GlobalScope.launch(Dispatchers.Main) {
        api.addBarcodeProduct(barcode)
    }

    suspend fun timeQueue(): Int {
        store.location = LatLng(50.25, 150.25)
        return api.timeQueue(store.location!!).data
    }

    suspend fun getProductImages(): List<String> {
        return if (store.selectedProduct!!.barcode != null)
            api.getProductImageBarcode(store.selectedProduct!!.barcode!!).data
        else
            api.getProductImageProductId(store.selectedProduct!!.id).data
    }
}