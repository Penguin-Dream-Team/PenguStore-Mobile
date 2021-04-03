package store.pengu.mobile.states

import android.location.Location
import androidx.compose.runtime.*
import com.google.android.gms.maps.model.LatLng
import store.pengu.mobile.data.ListTypes
import store.pengu.mobile.data.Product
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList

class StoreState {
    var userType by mutableStateOf("")
    var token by mutableStateOf("")

    lateinit var products: List<Product>

    lateinit var pantryLists: List<PantryList>

    lateinit var shoppingLists: List<ShoppingList>

    lateinit var lists: Array<List<ListTypes>>

    lateinit var selectedList: ListTypes

    lateinit var listLocation: LatLng
}

