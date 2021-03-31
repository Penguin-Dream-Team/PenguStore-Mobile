package store.pengu.mobile.states

import androidx.compose.runtime.*
import store.pengu.mobile.data.Product
import store.pengu.mobile.data.Pantry

class StoreState {
    var userType by mutableStateOf("")
    var token by mutableStateOf("")
    var selectedPantry by mutableStateOf(Pantry(-1, "", ""))

    var products = mutableStateListOf<Product>()
    var pantries = mutableStateListOf<Pantry>()
}

