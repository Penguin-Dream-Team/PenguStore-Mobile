package store.pengu.mobile.views.products.AddProductToListView.bottommenu

import android.view.LayoutInflater
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.ktor.util.*
import store.pengu.mobile.R
import store.pengu.mobile.services.CameraService
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.loading.RequestCameraPermission
import store.pengu.mobile.views.partials.IconButton

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ScanListBottomMenu(
    goBack: () -> Unit,
    title: String,
    onScan: (String) -> Unit,
    snackbarController: SnackbarController,
    cameraService: CameraService,
    onSearch: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { goBack() },
                icon = Icons.Filled.ArrowBack,
                description = "close scan popup"
            )
            Text(
                text = "Scan ${title.toLowerCase(Locale.current)}",
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold
            )
        }

        RequestCameraPermission(
            snackbarController,
            onFail = {
                goBack()
            }
        ) {
            AndroidView(factory = {
                LayoutInflater.from(it).inflate(R.layout.camera_layout, null)
            }) { inflatedLayout ->
                cameraService.initCamera(
                    context,
                    lifecycleOwner,
                    inflatedLayout as PreviewView,
                    onSuccess = onScan,
                    onFail = {
                        Toast.makeText(context, "No correct code found", Toast.LENGTH_SHORT).show()
                        //snackbarController.showDismissibleSnackbar("No correct code found")
                    },
                    CameraService.ScanType.LIST_CODE
                )
            }
        }
    }
}


