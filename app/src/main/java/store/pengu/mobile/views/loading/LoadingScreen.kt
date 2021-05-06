package store.pengu.mobile.views.loading

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.UserList
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.MapsService
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.launcherForActivityResult
import store.pengu.mobile.views.partials.pulltorefresh.LoadingProgressIndicator

@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@Composable
fun LoadingScreen(
    navController: NavController,
    listsService: ListsService,
    snackbarController: SnackbarController,
    mapsService: MapsService
) {
    val coroutineScope = rememberCoroutineScope()
    val (loading, setLoading) = remember { mutableStateOf(true) }
    var type: UserListType? by remember { mutableStateOf(null) }
    var list: UserList? by remember { mutableStateOf(null) }
    val (location, setLocation) = remember { mutableStateOf(null as LatLng?) }

    /**
     * If there is a list in my location show list
     */
    if (loading) {
        RequestLocation(snackbarController, loading, setLoading, setLocation, mapsService)
    } else {
        AnimatedVisibility(visible = !loading) {
            coroutineScope.launch {
                if (location != null) {
                    val pair =
                        listsService.findListInLocation(location.latitude, location.longitude)
                    type = pair?.first
                    list = pair?.second
                }

                navController.backStack.clear()
                when (type) {
                    UserListType.PANTRY -> {
                        listsService.getPantryLists()
                        navController.navigate("pantry_list/${list?.id}")
                    }
                    UserListType.SHOPPING_LIST -> {
                        listsService.getShoppingLists()
                        navController.navigate("shopping_list/${list?.id}")
                    }
                    null -> navController.navigate("lists")
                }
            }
        }
    }
}
