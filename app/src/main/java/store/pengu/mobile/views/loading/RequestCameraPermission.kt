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
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.launcherForActivityResult

@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@Composable
fun RequestCameraPermission(
    snackbarController: SnackbarController,
    onFail: () -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var loading by remember { mutableStateOf(true) }
    var canUseCamera by remember { mutableStateOf(false) }

    val cannotAccessCamera = stringResource(R.string.cannot_access_camera)

    /**
     * If there is a list in my location show list
     */

    val launcher =
        launcherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                snackbarController.showDismissibleSnackbar(cannotAccessCamera)
                loading = false
                onFail()
            }

            canUseCamera = granted
        }


    AnimatedVisibility(visible = loading) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) -> {
                canUseCamera = true
            }
            else -> {
                coroutineScope.launch {
                    // needs to wait for launcher to finish initializing
                    delay(100L)
                    launcher.launch(Manifest.permission.CAMERA)
                }
            }
        }
        loading = false

        Text(stringResource(R.string.loading))
    }

    AnimatedVisibility(visible = !loading && canUseCamera) {
        content()
    }
}
