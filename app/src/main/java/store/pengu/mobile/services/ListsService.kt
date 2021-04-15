package store.pengu.mobile.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.states.StoreState

class ListsService(
    private val api: PenguStoreApi,
    private val productsService: ProductsService,
    private val store: StoreState
) {

    suspend fun findListInLocation(latitude: Float, longitude: Float): UserListType? {
        return try {
            with(api.findList(latitude, longitude)) {
                store.selectedList = when (type) {
                    UserListType.PANTRY ->
                        PantryList(
                            (list["id"] as Int).toLong(),
                            list["code"] as String,
                            list["name"] as String,
                            (list["latitude"] as Double).toFloat(),
                            (list["longitude"] as Double).toFloat(),
                            list["productCount"] as Int
                        )
                    UserListType.SHOPPING_LIST -> {
                        ShoppingList(
                            (list["id"] as Int).toLong(),
                            list["name"] as String,
                            (list["latitude"] as Double).toFloat(),
                            (list["longitude"] as Double).toFloat()
                        )
                    }
                }
                type
            }
        } catch (e: PenguStoreApiException) {
            // No list found in location, so just load default view
            null
        }
    }


    fun createList(listName: String) = GlobalScope.launch(Dispatchers.Main) {
        if (store.listType == 0) {
            //val pantry = api.addPantry(listName, store.listLocation).data
            val pantry = PantryList(
                1,
                listName,
                "CODE",
                store.listLocation.latitude.toFloat(),
                store.listLocation.longitude.toFloat(),
                0
            )
            store.pantryLists.add(pantry)
            //refreshPantryList(pantry)
            //refreshPantryList(store.userId)

            if (store.selectedProduct != -1L) {
                productsService.addProduct(
                    pantry.id,
                    store.selectedProduct,
                    store.amountAvailable,
                    store.amountNeeded
                )
            }
        } //else {
        // api.addShoppingList(listName, store.listLocation)
        // refreshShoppingList(store.userId)
        //}
    }

    fun refreshPantryList(pantry: PantryList) = GlobalScope.launch(Dispatchers.Main) {
        store.pantryLists.add(pantry)
        store.lists[0] = store.pantryLists
    }

    fun refreshPantryList(userId: Long) = GlobalScope.launch(Dispatchers.Main) {
        //store.pantryLists.clear()
        //store.pantryLists.addAll(api.getUserPantries(userId).data)
        store.lists[0] = store.pantryLists
    }

    fun refreshShoppingList(userId: Long) = GlobalScope.launch(Dispatchers.Main) {
        store.shoppingLists.clear()
        store.shoppingLists.addAll(api.getUserShoppingLists(userId).data)
        store.lists[1] = emptyList()//store.shoppingLists
    }
}