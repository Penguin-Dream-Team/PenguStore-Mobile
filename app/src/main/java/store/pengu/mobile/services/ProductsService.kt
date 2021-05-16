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
import store.pengu.mobile.data.productlists.ProductListEntry
import store.pengu.mobile.data.productlists.ProductPantryListEntry
import store.pengu.mobile.data.productlists.ProductShoppingListEntry
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.storage.PenguCache

class ProductsService(
    private val api: PenguStoreApi,
    private val store: StoreState
) {
    private val pantryProducts = mutableMapOf<Long, SnapshotStateList<ProductInPantry>>()
    private val shoppingProducts = mutableMapOf<Long, SnapshotStateList<ProductInShoppingList>>()
    private val allProducts = mutableStateListOf<Product>()

    private val missingPantryProducts = mutableMapOf<Long, SnapshotStateList<Product>>()

    fun getMissingPantryProducts(pantryId: Long): SnapshotStateList<Product> {
        if (!missingPantryProducts.contains(pantryId)) {
            missingPantryProducts[pantryId] = mutableStateListOf()
        }
        return missingPantryProducts[pantryId]!!
    }

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

    suspend fun fetchMissingPantryProducts(pantryId: Long) {
        try {
            val received = api.getPantryMissingProducts(pantryId).data
            if (!pantryProducts.contains(pantryId)) {
                pantryProducts[pantryId] = mutableStateListOf()
            }
            updateMissingList(received, missingPantryProducts[pantryId]!!)
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

    private fun updateMissingList(
        received: List<Product>,
        listToUpdate: SnapshotStateList<Product>
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

    suspend fun createProduct(name: String, barcode: String?, image: String?): Product {
        val product = api.createProduct(name, barcode, image).data
        store.selectedProduct = product
        return product
    }

    private val productPantryLists = mutableMapOf<Long, SnapshotStateList<ProductPantryListEntry>>()
    private val productShoppingLists =
        mutableMapOf<Long, SnapshotStateList<ProductShoppingListEntry>>()

    suspend fun fetchProduct(productId: Long) {
        store.selectedProduct = api.getProduct(productId).data
    }

    fun getProductPantryLists(productId: Long): SnapshotStateList<ProductPantryListEntry> {
        if (!productPantryLists.contains(productId)) {
            productPantryLists[productId] = mutableStateListOf()
        }
        return productPantryLists[productId]!!
    }

    fun getProductShoppingLists(productId: Long): SnapshotStateList<ProductShoppingListEntry> {
        if (!productShoppingLists.contains(productId)) {
            productShoppingLists[productId] = mutableStateListOf()
        }
        return productShoppingLists[productId]!!
    }

    suspend fun addProductToPantryList(
        productId: Long,
        pantryId: Long,
        haveAmount: Int,
        needAmount: Int
    ) {
        try {
            val received =
                api.addProductToPantryList(productId, pantryId, haveAmount, needAmount).data
            if (!productPantryLists.contains(productId)) {
                productPantryLists[productId] = mutableStateListOf()
            }
            updateProductList(received, productPantryLists[productId]!!)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    suspend fun addProductToShoppingList(
        productId: Long,
        shoppingListId: Long,
        price: Double
    ) {
        try {
            val received = api.addProductToShoppingList(productId, shoppingListId, price).data
            if (!productShoppingLists.contains(productId)) {
                productShoppingLists[productId] = mutableStateListOf()
            }
            updateProductList(received, productShoppingLists[productId]!!)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    suspend fun fetchProductPantryLists(productId: Long) {
        try {
            val received = api.getProductPantryLists(productId).data
            if (!productPantryLists.contains(productId)) {
                productPantryLists[productId] = mutableStateListOf()
            }
            updateProductList(received, productPantryLists[productId]!!)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    suspend fun fetchProductShoppingLists(productId: Long) {
        try {
            val received = api.getProductShoppingLists(productId).data
            if (!productShoppingLists.contains(productId)) {
                productShoppingLists[productId] = mutableStateListOf()
            }
            updateProductList(received, productShoppingLists[productId]!!)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    private fun <T : ProductListEntry> updateProductList(
        received: List<T>,
        listToUpdate: SnapshotStateList<T>
    ) {
        listToUpdate.removeIf { old ->
            received.find { new ->
                old.listId == new.listId
            } == null
        }
        listToUpdate.replaceAll { old ->
            received.find { new ->
                old.listId == new.listId
            }!!
        }
        listToUpdate.addAll(received.filterNot { new ->
            listToUpdate.contains(new)
        })
    }

    private fun <T : ProductListEntry> updateProductList(
        received: T,
        listToUpdate: SnapshotStateList<T>
    ) {
        val index = listToUpdate.indexOfFirst { it.listId == received.listId }
        if (index != -1) {
            listToUpdate[index] = received
        } else {
            listToUpdate.add(received)
        }
    }

    private val productImages =
        mutableMapOf<Long, SnapshotStateList<String>>()


    fun getProductImages(productId: Long): SnapshotStateList<String> {
        if (!productImages.contains(productId)) {
            productImages[productId] = mutableStateListOf()
        }
        return productImages[productId]!!
    }

    suspend fun fetchProductImages(productId: Long) {
        try {
            val received =
                api.getProductImages(productId).data
            if (!productPantryLists.contains(productId)) {
                productPantryLists[productId] = mutableStateListOf()
            }
            updateProductImagesList(received, productImages[productId]!!)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    private fun updateProductImagesList(
        received: List<String>,
        listToUpdate: SnapshotStateList<String>
    ) {
        listToUpdate.removeIf { old ->
            !received.contains(old)
        }
        listToUpdate.replaceAll { old ->
            received.find { new ->
                new == old
            }!!
        }
        listToUpdate.addAll(received.filterNot { new ->
            listToUpdate.contains(new)
        })
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

    fun putImageCache(){
        // TODO
        //PenguCache.putAllImage(store.selectedProduct!!.id.toString(), )
    }

    fun getAllImagesCache(): List<String> {
        return PenguCache.getAllImage(store.selectedProduct!!.id.toString()) ?: listOf<String>()
    }
}