package store.pengu.mobile.states

import androidx.compose.runtime.*
import store.pengu.mobile.data.Item

class StoreState {
    var userType by mutableStateOf("")
    var token by mutableStateOf("")

    var items = mutableStateListOf(
        Item ("Pão", "Comida", 4, 3),
        Item("Arroz", "Comida", 4, 3),
        Item("Batata", "Comida", 4, 3)
    )
}

