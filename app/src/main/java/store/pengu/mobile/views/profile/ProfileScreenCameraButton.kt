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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.launcherForActivityResult

@ExperimentalAnimationApi
@Composable
fun ProfileScreenCameraButton(
    snackbarController: SnackbarController
) {
    val context = LocalContext.current
    var canUseCamera by remember { mutableStateOf(false) }
    var needsCameraPermission by remember { mutableStateOf(true) }
    var canGrantPermission by remember { mutableStateOf(true) }

    val launcher =
        launcherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                snackbarController.showDismissibleSnackbar("Cannot use camera")
                canGrantPermission = true
            }
            canUseCamera = granted
        }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        if (needsCameraPermission) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) -> {
                    canUseCamera = true
                }
            }
            needsCameraPermission = false
        }

        AnimatedVisibility(visible = !canUseCamera) {
            Button(
                onClick = {
                    canGrantPermission = false
                    launcher.launch(Manifest.permission.CAMERA)
                },
                enabled = canGrantPermission,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Grant Camera Access")
            }
        }
        AnimatedVisibility(visible = canUseCamera) {
            Button(
                onClick = { },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Granted Camera Access")
            }
        }
    }
}
