package store.pengu.mobile.states

import androidx.compose.runtime.*
import store.pengu.mobile.data.ListTypes
import store.pengu.mobile.data.Product
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList

class StoreState {
    var userType by mutableStateOf("")
    var token by mutableStateOf("")

    lateinit var products: List<Product>

    lateinit var pantries: List<PantryList>

    lateinit var shoppingLists: List<ShoppingList>

    lateinit var selectedList: ListTypes
}

