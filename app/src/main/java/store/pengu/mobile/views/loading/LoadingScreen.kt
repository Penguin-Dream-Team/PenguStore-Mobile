package store.pengu.mobile.views.loading

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.launch
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.services.ListsService

@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@Composable
fun LoadingScreen(
    navController: NavController,
    listsService: ListsService
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
        navController.backStack.clear()
        when (type) {
            UserListType.PANTRY ->
                navController.navigate("pantry_list")
            UserListType.SHOPPING_LIST ->
                navController.navigate("shopping_list")
            null -> navController.navigate("lists")
        }
    } else {
        Text("Loaded")
    }
}