package store.pengu.mobile.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.states.StoreState

class ListsService(
    private val api: PenguStoreApi,
    private val store: StoreState
) {

    fun createList(listName: String) = GlobalScope.launch(Dispatchers.Main) {
        api.addPantry(listName, store.listLocation)
        api.addUserPantry(store.userId, "abc")
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