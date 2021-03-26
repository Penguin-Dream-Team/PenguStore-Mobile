package store.pengu.mobile.states

import androidx.compose.runtime.*
import store.pengu.mobile.data.Product
import store.pengu.mobile.data.Pantry

class StoreState {
    var userType by mutableStateOf("")
    var token by mutableStateOf("")

    var products = mutableStateListOf(
        Product(1, "Pão", "123", 3.0, 1),
        Product(2, "Arroz", "123", 3.0, 1),
        Product(3, "Batata", "123", 3.0, 1)
    )

    var pantries = mutableStateListOf(
        Pantry(1, "123", "Pantry 1"),
        Pantry(2, "123", "Pantry 2"),
        Pantry(3, "123", "Pantry 3"),
    )
}

