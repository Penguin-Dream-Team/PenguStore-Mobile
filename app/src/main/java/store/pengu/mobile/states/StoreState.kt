package store.pengu.mobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class StoreState {
    var userType by mutableStateOf("")
    var token by mutableStateOf("")
}