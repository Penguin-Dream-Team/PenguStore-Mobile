package store.pengu.mobile.services

import androidx.compose.runtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.api.requests.lists.CartRequest
import store.pengu.mobile.data.CartProduct
import store.pengu.mobile.states.StoreState

class CartService(
    private val api: PenguStoreApi,
    private val listsService: ListsService,
    private val store: StoreState
) {

    fun buyCart() = GlobalScope.launch(Dispatchers.Main) {
        val requests = mutableStateListOf<CartProduct>()
        store.cartProducts.forEach { entry ->
            entry.value.forEach { product ->
                val cartProduct = CartProduct(product.productId, entry.key, product.inCart.value)
                requests.add(cartProduct)
            }
        }
        try {
            api.buyCart(CartRequest(requests))
            store.cartProducts.clear()
            store.cartShoppingList = null
            listsService.getPantryLists()
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }
}