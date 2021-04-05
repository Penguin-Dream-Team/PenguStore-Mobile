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
        store.products = api.products().data
    }

    fun addProduct(
        pantryId: Long,
        productId: Long,
        amountAvailable: Int,
        amountNeeded: Int
    ) = GlobalScope.launch(Dispatchers.Main) {
        api.addPantryProduct(pantryId, productId, amountAvailable, amountNeeded)
    }
}