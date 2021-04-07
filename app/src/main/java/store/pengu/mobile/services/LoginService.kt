package store.pengu.mobile.services

import android.provider.Settings.Secure
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.states.StoreState

class LoginService(
    private val api: PenguStoreApi,
    private val store: StoreState
) {

    fun login() = GlobalScope.launch(Dispatchers.Main) { }

    fun guestLogin(navController: NavController) = GlobalScope.launch(Dispatchers.Main) {
        //val androidId = Secure.getString(coroutineContext.getContentResolver(), Settings.Secure.ANDROID_ID)
        //val user = api.guestLogin(androidId).data
        val userId = 6L

        store.userId = userId
        store.pantryLists = api.getUserPantries(userId).data
        store.shoppingLists = api.getUserShoppingLists(userId).data
        store.lists = arrayOf(store.pantryLists, store.shoppingLists)

        navController.navigate("dashboard")
    }

    val userId: Long get() = store.userId
    fun logout() = store.logout()
}