package store.pengu.mobile.views.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import kotlinx.coroutines.launch
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.utils.SnackbarController

@SuppressLint("RestrictedApi")
@ExperimentalComposeUiApi
@Composable
fun LoginScreen(
    navController: NavController,
    accountService: AccountService,
    snackbarController: SnackbarController
) {
    val coroutineScope = rememberCoroutineScope()
    val canRegister = remember { mutableStateOf(true) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val attemptLogin: (suspend () -> String) -> Unit = { login ->
        keyboardController?.hide()
        canRegister.value = false
        coroutineScope.launch {
            try {
                val username = login()
                navController.backStack.clear()
                navController.navigate("loading")
                snackbarController.showDismissibleSnackbar("Welcome $username")
            } catch (e: PenguStoreApiException) {
                snackbarController.showDismissibleSnackbar(e.message)
                canRegister.value = true
            }
        }
    }

    /**
     * Uncomment to enable automatic login for testing
     */
    /*Box {
        val ok = remember {
            mutableStateOf(coroutineScope.launch {
                accountService.login("Tux", "tux")
                navController.navigate("loading")
            })
        }
    }*/
/*
    Box {
        val ok = remember {
            mutableStateOf(coroutineScope.launch {
                accountService.registerGuest()
                navController.navigate("loading")
            })
        }
    }
*/

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        LoginScreenLogo()

        LoginScreenForm(
            accountService,
            canRegister,
            attemptLogin
        )

        LoginScreenBottomLink(
            accountService,
            canRegister,
            attemptLogin
        )
    }
}
