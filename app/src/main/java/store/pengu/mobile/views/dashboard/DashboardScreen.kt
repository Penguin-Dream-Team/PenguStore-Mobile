package store.pengu.mobile.views.dashboard

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.TermiteService

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun DashboardScreen(
    navController: NavController,
    listsService: ListsService,
    termiteService: TermiteService
) {
    //val storeState by remember { mutableStateOf(store) }
    val coroutineScope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(true) }
    var type: UserListType? by remember { mutableStateOf(null) }

    /**
     * If there is a list in my location show list
     */

    AnimatedVisibility(visible = loading) {
        coroutineScope.launch {
            val latitude = 150.0f
            val longitude = 150.0f
            type = listsService.findListInLocation(latitude, longitude)
            loading = false
        }
        Text("Loading")
    }

    if (!loading) {
        GlobalScope.launch(Dispatchers.Main) {
            termiteService.turnWifiDirectOn()
        }

        when (type) {
            UserListType.PANTRY ->
                navController.navigate("pantry_list")
            UserListType.SHOPPING_LIST ->
                navController.navigate("shopping_list")
            null -> Text("No list in location")
        }
    } else {
        Text("Loaded")
    }
}
