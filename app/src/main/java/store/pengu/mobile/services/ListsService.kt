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
        api.addPantry(store.userId, listName, store.listLocation)
    }
}