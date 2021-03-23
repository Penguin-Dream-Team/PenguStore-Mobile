package store.pengu.mobile.states

import androidx.compose.runtime.*
import store.pengu.mobile.data.Item
class StoreState {
    var userType by mutableStateOf("")
    var items: List<Item> by mutableStateOf( List(3) {
        Item ("Pão", "Comida", 4, 3)
        Item("Arroz", "Comida", 4, 3)
        Item("Batata", "Comida", 4, 3)
    })
}

