package store.pengu.mobile.services

import androidx.compose.runtime.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.lists.AvailableListColor

class ListsService(
    private val api: PenguStoreApi,
    private val productsService: ProductsService,
    private val store: StoreState
) {

    val newListName = mutableStateOf("")
    val newListColor = mutableStateOf(AvailableListColor.BLUE)
    val newListLocation: MutableState<LatLng?> = mutableStateOf(null)

    val pantryLists = mutableStateListOf<PantryList>()
    val shoppingLists = mutableStateListOf<ShoppingList>()

    private var isCreating = mutableStateOf(false)

    fun newCanPickLocation(): Boolean {
        return newListName.value.isNotBlank()
    }

    fun newCanCreate(): Boolean {
        return !isCreating.value && newCanPickLocation() && newListLocation.value != null
    }

    fun resetNewListData() {
        newListName.value = ""
        newListColor.value = AvailableListColor.BLUE
        newListLocation.value = null
    }

    suspend fun getPantryLists() {
        try {
            pantryLists.clear()
            pantryLists.addAll(api.getPantryLists().data)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    suspend fun getShoppingLists() {
        try {
            shoppingLists.clear()
            shoppingLists.addAll(api.getShoppingLists().data)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    /**
     * @throws PenguStoreApiException
     */
    suspend fun createNewPantryList() {
        if (isCreating.value) {
            return
        }

        isCreating.value = true
        pantryLists.add(
            api.createPantryList(
                newListName.value,
                newListLocation.value!!,
                newListColor.value
            ).data
        )
        resetNewListData()
        isCreating.value = false
    }

    /**
     * @throws PenguStoreApiException
     */
    suspend fun createNewShoppingList() {
        if (isCreating.value) {
            return
        }

        isCreating.value = true
        shoppingLists.add(
            api.createShoppingList(
                newListName.value,
                newListLocation.value!!,
                newListColor.value
            ).data
        )
        resetNewListData()
        isCreating.value = false
    }

    suspend fun findListInLocation(latitude: Float, longitude: Float): UserListType? {
        return try {
            with(api.findList(latitude, longitude)) {
                store.selectedList = when (type) {
                    UserListType.PANTRY ->
                        PantryList(
                            (list["id"] as Int).toLong(),
                            list["code"] as String,
                            list["name"] as String,
                            list["latitude"] as Double,
                            list["longitude"] as Double,
                            list["productCount"] as Int,
                            list["color"] as String,
                            list["shared"] as Boolean
                        )
                    UserListType.SHOPPING_LIST -> {
                        ShoppingList(
                            (list["id"] as Int).toLong(),
                            list["name"] as String,
                            list["latitude"] as Double,
                            list["longitude"] as Double,
                            list["color"] as String,
                            list["shared"] as Boolean
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

}