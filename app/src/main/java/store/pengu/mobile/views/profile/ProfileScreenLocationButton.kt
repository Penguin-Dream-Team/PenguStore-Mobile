package store.pengu.mobile.views.profile

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import store.pengu.mobile.R
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.launcherForActivityResult

@ExperimentalAnimationApi
@Composable
fun ProfileScreenLocationButton(
    snackbarController: SnackbarController
) {
    val context = LocalContext.current
    var canGetLocation by remember { mutableStateOf(false) }
    var needsLocationPermission by remember { mutableStateOf(true) }
    var canGrantPermission by remember { mutableStateOf(true) }

    val cannotFetchCurrentLocation = stringResource(R.string.cannot_fetch_current_location)

    val launcher =
        launcherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                snackbarController.showDismissibleSnackbar(cannotFetchCurrentLocation)
                canGrantPermission = true
            }
            canGetLocation = granted
        }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        if (needsLocationPermission) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    canGetLocation = true
                }
            }
            needsLocationPermission = false
        }

        AnimatedVisibility(visible = !canGetLocation) {
            Button(
                onClick = {
                    canGrantPermission = false
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                enabled = canGrantPermission,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.grant_location_access))
            }
        }
        AnimatedVisibility(visible = canGetLocation) {
            Button(
                onClick = { },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.granted_location_access))
            }
        }
    }
}
