package store.pengu.mobile.views.profile

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController


@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@Composable
fun ProfileScreenFooter(
    navController: NavController,
    accountService: AccountService,
    snackbarController: SnackbarController,
    store: StoreState,
    coroutineScope: CoroutineScope
) {
    var showAlert by remember { mutableStateOf(false) }
    val dismissAlert: () -> Unit = {
        showAlert = false
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedVisibility(visible = store.isLoggedIn()) {
            Button(
                onClick = {
                    showAlert = true
                },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Logout")
            }
        }

        AnimatedVisibility(visible = showAlert) {
            AlertDialog(
                onDismissRequest = {
                    dismissAlert()
                },
                confirmButton = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    accountService.logout()
                                    navController.backStack.clear()
                                    navController.navigate("login")
                                    snackbarController.showDismissibleSnackbar("Logged out")
                                } catch (e: PenguStoreApiException) {
                                    snackbarController.showDismissibleSnackbar(e.message)
                                }
                            }
                        }) {
                        Text("Logout")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = dismissAlert
                    ) {
                        Text("Dismiss")
                    }
                },
                title = {
                    Text("Are you sure you want to logout?")
                },
                text = {
                    Text("You will lose access to your account and all data")
                }
            )
        }
    }
}
