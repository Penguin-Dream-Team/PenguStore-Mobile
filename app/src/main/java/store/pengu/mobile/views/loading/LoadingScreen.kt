package store.pengu.mobile.views.loading

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.launcherForActivityResult

@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@Composable
fun LoadingScreen(
    navController: NavController,
    listsService: ListsService,
    snackbarController: SnackbarController
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var loading by remember { mutableStateOf(true) }
    var type: UserListType? by remember { mutableStateOf(null) }
    var canGetLocation by remember { mutableStateOf(false) }
    var needsLocationPermission by remember { mutableStateOf(true) }

    /**
     * If there is a list in my location show list
     */

    val launcher =
        launcherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                snackbarController.showDismissibleSnackbar("Cannot fetch current location")
                loading = false
            }
            canGetLocation = granted
        }


    AnimatedVisibility(visible = loading) {
        if (needsLocationPermission) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    canGetLocation = true
                }
                else -> {
                    coroutineScope.launch {
                        // needs to wait for launcher to finish initializing
                        delay(100L)
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
            needsLocationPermission = false
        }

        if (canGetLocation) {
            val fusedLocationClient =
                remember { LocationServices.getFusedLocationProviderClient(context) }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                coroutineScope.launch {
                    if (location != null) {
                        type =
                            listsService.findListInLocation(location.latitude, location.longitude)
                    }
                    loading = false
                }
            }
        }
        Text("Loading...")
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
