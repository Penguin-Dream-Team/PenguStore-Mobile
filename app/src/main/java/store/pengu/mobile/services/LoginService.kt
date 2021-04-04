package store.pengu.mobile.services

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.states.StoreState

class LoginService(
    private val api: PenguStoreApi,
    private val store: StoreState
) {

    fun login() = GlobalScope.launch(Dispatchers.Main) { }

    fun guestLogin(navController: NavController) = GlobalScope.launch(Dispatchers.Main) {
        val userId = 69L //api.guestLogin().data

        store.userId = userId
        store.pantryLists = api.getUserPantries(userId).data
        store.shoppingLists = api.getUserShoppingLists(userId).data
        store.lists = arrayOf(store.pantryLists, store.shoppingLists)

        delay(1000)
        navController.navigate("dashboard")
    }

    val userId: Long get() = store.userId
    fun logout() = store.logout()
}