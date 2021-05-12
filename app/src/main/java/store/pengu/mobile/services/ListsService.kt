package store.pengu.mobile.services

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.gms.maps.model.LatLng
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.data.UserList
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.lists.AvailableListColor

class ListsService(
    private val api: PenguStoreApi,
    private val store: StoreState
) {

    val newListName = mutableStateOf("")
    val newListColor = mutableStateOf(AvailableListColor.BLUE)
    val newListLocation: MutableState<LatLng?> = mutableStateOf(null)

    val newListCode = mutableStateOf("")

    val pantryLists = mutableStateListOf<PantryList>()
    val shoppingLists = mutableStateListOf<ShoppingList>()

    private var isCreating = mutableStateOf(false)
    private var isImporting = mutableStateOf(false)

    fun getList(type: UserListType, id: Long): UserList? {
        return when (type) {
            UserListType.PANTRY -> pantryLists.find { it.id == id }
            UserListType.SHOPPING_LIST -> shoppingLists.find { it.id == id }
        }
    }

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

    fun resetImportListData() {
        newListCode.value = ""
    }

    fun newCanImportList(): Boolean {
        return !isImporting.value && newListCode.value.isNotBlank()
    }

    suspend fun getPantryLists() {
        try {
            val received = api.getPantryLists().data
            updateList(received, pantryLists)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    suspend fun getShoppingLists() {
        try {
            val received = api.getShoppingLists().data
            updateList(received, shoppingLists)
        } catch (e: Exception) {
            // fetch from cache
            e.printStackTrace()
        }
    }

    private fun <T : UserList> updateList(received: List<T>, listToUpdate: SnapshotStateList<T>) {
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
    suspend fun importNewPantryList() {
        if (isImporting.value) {
            return
        }

        isImporting.value = true
        try {
            pantryLists.add(
                api.importPantry(
                    newListCode.value,
                ).data
            )
            resetImportListData()
            isImporting.value = false
        } catch (e: PenguStoreApiException) {
            isImporting.value = false
            throw e
        }
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

    /**
     * @throws PenguStoreApiException
     */
    suspend fun importNewShoppingList() {
        if (isImporting.value) {
            return
        }

        isImporting.value = true
        try {
            shoppingLists.add(
                api.importShoppingList(
                    newListCode.value,
                ).data
            )
            resetImportListData()
            isImporting.value = false
        } catch (e: PenguStoreApiException) {
            isImporting.value = false
            throw e
        }
    }

    suspend fun findListInLocation(
        latitude: Double,
        longitude: Double
    ): Pair<UserListType, UserList>? {
        return try {
            with(api.findList(latitude, longitude)) {
                val list = when (type) {
                    UserListType.PANTRY ->
                        PantryList(
                            (list["id"] as Int).toLong(),
                            list["name"] as String,
                            list["code"] as String,
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
                            list["code"] as String,
                            list["latitude"] as Double,
                            list["longitude"] as Double,
                            list["color"] as String,
                            list["shared"] as Boolean,
                            list["productCount"] as Int
                        )
                    }
                }
                type to list
            }
        } catch (e: PenguStoreApiException) {
            // No list found in location, so just load default view
            null
        }
    }
}