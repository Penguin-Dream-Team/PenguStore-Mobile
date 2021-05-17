package store.pengu.mobile.views.loading

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.UserList
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.MapsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.partials.pulltorefresh.LoadingProgressIndicator

@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@Composable
fun LoadingScreen(
    navController: NavController,
    listsService: ListsService,
    snackbarController: SnackbarController,
    mapsService: MapsService,
    accountService: AccountService,
    store: StoreState
) {
    val coroutineScope = rememberCoroutineScope()
    val (loading, setLoading) = remember { mutableStateOf(true) }
    var needsLoadData by remember { mutableStateOf(true) }
    var canGetLoadData by remember { mutableStateOf(true) }
    var type: UserListType? by remember { mutableStateOf(null) }
    var list: UserList? by remember { mutableStateOf(null) }
    var serverConnection by remember { mutableStateOf(true) }

    val location = store.location

    val cant_reach_server = stringResource(R.string.cant_reach_server)

    /**
     * If there is a list in my location show list
     */
    if (loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (needsLoadData) {
                needsLoadData = false
                coroutineScope.launch {
                    try {
                        if (canGetLoadData) {
                            canGetLoadData = false
                            accountService.loadData()
                            if (store.isLoggedIn()) {
                                delay(3000)
                                setLoading(false)
                            } else {
                                navController.navigate("login")
                            }
                        }
                    } catch (e: PenguStoreApiException) {
                        snackbarController.showDismissibleSnackbar(cant_reach_server)
                        serverConnection = false
                    }
                    canGetLoadData = true
                }
            }
            if (serverConnection && location == null) {
                LoadingProgressIndicator(
                    progressColor = MaterialTheme.colors.primary,
                    backgroundColor = MaterialTheme.colors.surface
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = stringResource(R.string.loading)
                )
            } else if (location != null) {
                setLoading(false)
            } else if(!serverConnection) {
                Button(onClick = {
                    needsLoadData = true
                    serverConnection = true
                }) {
                    Text(stringResource(R.string.try_again))
                }
            }
        }
    } else {
        AnimatedVisibility(visible = !loading) {
            coroutineScope.launch {
                try {
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
                } catch (e: PenguStoreApiException) {
                    snackbarController.showDismissibleSnackbar(cant_reach_server)
                    serverConnection = false
                }
            }
        }
    }
}
