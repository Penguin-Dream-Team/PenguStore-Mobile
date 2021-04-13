package store.pengu.mobile.views.profile

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController

@ExperimentalComposeUiApi
@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@Composable
fun ProfileScreen(
    navController: NavController,
    accountService: AccountService,
    snackbarController: SnackbarController,
    store: StoreState,
) {
    val coroutineScope = rememberCoroutineScope()
    var loggedIn by remember { mutableStateOf(store.isLoggedIn()) }
    Box {
        if (!loggedIn) {
            loggedIn = true
            coroutineScope.launch {
                accountService.registerGuest()
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
    ) {
        ProfileScreenInformation(
            navController,
            accountService,
            snackbarController,
            store,
            coroutineScope
        )

        ProfileScreenAccountCreation(
            navController,
            accountService,
            snackbarController,
            store,
            coroutineScope
        )

        ProfileScreenFooter(
            navController,
            accountService,
            snackbarController,
            store,
            coroutineScope
        )
    }
}