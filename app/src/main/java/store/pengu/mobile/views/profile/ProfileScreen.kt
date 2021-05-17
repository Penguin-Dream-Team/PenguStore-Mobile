package store.pengu.mobile.views.profile

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    context: Context,
    store: StoreState,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileScreenInformation(
            accountService,
            snackbarController,
            store,
            coroutineScope
        )

        ProfileScreenAccountCreation(
            accountService,
            snackbarController,
            store,
            context,
            coroutineScope
        )

        ProfileScreenLocationButton(snackbarController)
        ProfileScreenCameraButton(snackbarController)

        ProfileScreenFooter(
            navController,
            accountService,
            snackbarController,
            store,
            coroutineScope
        )
    }
}