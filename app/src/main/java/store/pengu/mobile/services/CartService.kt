package store.pengu.mobile.services

import androidx.compose.runtime.mutableStateListOf
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.api.requests.lists.CartRequest
import store.pengu.mobile.data.CartProduct
import store.pengu.mobile.states.StoreState

class CartService(
    private val api: PenguStoreApi,
    private val listsService: ListsService,
    private val store: StoreState
) {

    suspend fun buyCart() {
        val requests = mutableStateListOf<CartProduct>()
        store.cartProducts.forEach { entry ->
            entry.value.forEach { product ->
                val cartProduct = CartProduct(product.productId, entry.key, product.inCart.value)
                requests.add(cartProduct)
            }
        }
        api.buyCart(CartRequest(requests))
        store.cartProducts.clear()
        store.cartShoppingList = null
        listsService.getPantryLists()
    }
}