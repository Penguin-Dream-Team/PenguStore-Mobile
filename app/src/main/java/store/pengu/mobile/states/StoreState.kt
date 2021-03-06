package store.pengu.mobile.states

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.gms.maps.model.LatLng
import store.pengu.mobile.data.*

class StoreState {
    var token: String by mutableStateOf("")
    var username: String by mutableStateOf("")
    var guest: Boolean by mutableStateOf(true)
    var email: String by mutableStateOf("")
    var selectedListType = mutableStateOf(0)
    var selectedList: UserList? = null

    //var selectedProduct: Product? = null
    private val selectedProductState = mutableStateOf(null as Product?)
    val selectedProduct: Product? get() = selectedProductState.value
    val setSelectedProduct: (Product?) -> Unit get() = selectedProductState.component2()

    private val locationState = mutableStateOf(null as LatLng?)
    val location: LatLng? get() = locationState.value
    val setLocation: (LatLng?) -> Unit get() = locationState.component2()

    var cartShoppingList: Long? = null

    fun isLoggedIn(): Boolean {
        return token.isNotBlank()
    }

    /**
     * REWRITE
     */

    var shouldFindListInLocation: Boolean by mutableStateOf(true)
    var joinQueueTime: Int? = null

    var userId: Long by mutableStateOf(1)
    var products = mutableStateListOf<Product>()
    var pantryLists = mutableStateListOf<PantryList>()
    //var shoppingLists = mutableStateListOf<ShoppingList2>()
    var lists = Array(2) { emptyList<UserList>() }
    var listType = -1
    var amountAvailable: Int = 0
    var amountNeeded: Int = 0
    var pantryProducts = mutableStateListOf<ProductInPantry>()
    var shoppingListProducts = mutableStateListOf<ProductInShoppingList>()
    var cartProducts = mutableStateMapOf<Long, SnapshotStateList<MutableShopItem>>()

    var listLocation: LatLng? = null
}

