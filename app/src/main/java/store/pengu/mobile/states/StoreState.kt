package store.pengu.mobile.states

import androidx.compose.runtime.*
import com.google.android.gms.maps.model.LatLng
import store.pengu.mobile.data.ListTypes
import store.pengu.mobile.data.Product
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList

class StoreState {
    var userId: Long by mutableStateOf(-1)
    var token: String by mutableStateOf("")
    var products = emptyList<Product>()
    var pantryLists = emptyList<PantryList>()
    var shoppingLists = emptyList<ShoppingList>()
    var lists = Array(2) { emptyList<ListTypes>() }
    var listType = -1

    lateinit var selectedList: ListTypes
    lateinit var listLocation: LatLng

    fun logout() {
        token = ""
        // TODO ADD
    }
}

