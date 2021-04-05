package store.pengu.mobile.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.states.StoreState

class ListsService(
    private val api: PenguStoreApi,
    private val productsService: ProductsService,
    private val store: StoreState
) {

    fun createList(listName: String) = GlobalScope.launch(Dispatchers.Main) {
        if (store.listType == 0) {
            val pantry = api.addPantry(listName, store.listLocation)
            api.addUserPantry(store.userId, "abc")
            //refreshPantryList(pantry)
            refreshPantryList(store.userId)

            if (store.selectedProduct != -1L) {
                productsService.addProduct(
                    0, //pantry.id,
                    store.selectedProduct,
                    store.amountAvailable,
                    store.amountNeeded
                )
            }
        } //else {
            // api.addShoppingList(listName, store.listLocation)
            // refreshPantryList(store.userId)
        //}
    }

    fun refreshPantryList(pantry: PantryList) = GlobalScope.launch(Dispatchers.Main) {
        store.pantryLists += pantry
        store.lists[0] = store.pantryLists
    }

    fun refreshPantryList(userId: Long) = GlobalScope.launch(Dispatchers.Main) {
        store.pantryLists = api.getUserPantries(userId).data
        store.lists[0] = store.pantryLists
    }

    fun refreshShoppingList(userId: Long) = GlobalScope.launch(Dispatchers.Main) {
        store.shoppingLists = api.getUserShoppingLists(userId).data
        store.lists[1] = store.shoppingLists
    }
}