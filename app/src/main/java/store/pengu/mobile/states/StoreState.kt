package store.pengu.mobile.states

import androidx.compose.runtime.*
import com.google.android.gms.maps.model.LatLng
import store.pengu.mobile.data.UserList
import store.pengu.mobile.data.Product
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList2

class StoreState {
    var token: String by mutableStateOf("")
    var username: String by mutableStateOf("")
    var guest: Boolean by mutableStateOf(true)
    var email: String by mutableStateOf("")
    var selectedListType = mutableStateOf(0)

    fun isLoggedIn(): Boolean {
        return token.isNotBlank()
    }

    var shouldFindListInLocation: Boolean by mutableStateOf(true)

    var userId: Long by mutableStateOf(1)
    var products = mutableStateListOf<Product>()
    var pantryLists = mutableStateListOf<PantryList>()
    var shoppingLists = mutableStateListOf<ShoppingList2>()
    var lists = Array(2) { emptyList<UserList>() }
    var listType = -1
    var selectedProduct: Long = -1L
    var amountAvailable: Int = 0
    var amountNeeded: Int = 0
    var pantryProducts = mutableStateListOf<Product>()
    var shoppingListProducts = mutableStateListOf<Product>()
    var cartProducts = mutableStateListOf<Pair<Product, Int>>()

    var selectedList: UserList? = null
    var listLocation: LatLng? = null
}

