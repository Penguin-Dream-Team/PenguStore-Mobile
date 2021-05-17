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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import store.pengu.mobile.R
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
    val loggedOut = stringResource(R.string.logged_out)

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
                Text(text = stringResource(R.string.logout))
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
                                    snackbarController.showDismissibleSnackbar(loggedOut)
                                } catch (e: PenguStoreApiException) {
                                    snackbarController.showDismissibleSnackbar(e.message)
                                }
                            }
                        }) {
                        Text(stringResource(R.string.logout))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = dismissAlert
                    ) {
                        Text(stringResource(R.string.dismiss))
                    }
                },
                title = {
                    Text(stringResource(R.string.confirm_logout_text))
                },
                text = {
                    if (store.guest) {
                        Text(stringResource(R.string.lose_access_acc_data_text))
                    } else {
                        Text(stringResource(R.string.have_login_again_text))
                    }
                }
            )
        }
    }
}
