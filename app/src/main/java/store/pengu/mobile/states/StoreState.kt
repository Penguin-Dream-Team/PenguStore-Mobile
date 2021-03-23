package store.pengu.mobile.states

import androidx.compose.runtime.*
import store.pengu.mobile.data.Item
import store.pengu.mobile.data.Pantry
import store.pengu.mobile.data.User

class StoreState {
    var userType by mutableStateOf("")
    var token by mutableStateOf("")

    var items = mutableStateListOf(
        Item ("Pão", "Comida", 4, 3),
        Item("Arroz", "Comida", 4, 3),
        Item("Batata", "Comida", 4, 3)
    )

    var user = User(1, mutableStateListOf(
        Pantry("Pantry 1", false, mutableStateListOf(
            Item ("Pão", "Comida", 4, 3),
            Item("Arroz", "Comida", 4, 3),
        )),
        Pantry("Pantry 2", false, mutableStateListOf(
            Item ("Pão", "Comida", 4, 3),
            Item("Batata", "Comida", 4, 3)
        )),
        Pantry("Pantry 3", false, mutableStateListOf(
            Item("Arroz", "Comida", 4, 3),
            Item("Batata", "Comida", 4, 3)
        ))
    ))
}

