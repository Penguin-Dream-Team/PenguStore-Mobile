package store.pengu.mobile.views.lists.partials

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.CoilImage
import store.pengu.mobile.R
import store.pengu.mobile.data.UserList
import store.pengu.mobile.utils.QRCodeUtils
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.partials.AnimatedShimmerLoading

@Suppress("UNUSED_VALUE")
@ExperimentalAnimationApi
@Composable
fun ShareList(
    list: UserList,
    snackbarController: SnackbarController,
    type: String
) {
    Divider()

    val clipboardManager = LocalClipboardManager.current
    val interactionSource = remember { MutableInteractionSource() }
    var isCopying by remember { mutableStateOf(false) }

    val shouldCopy = interactionSource.collectIsPressedAsState().value

    if (!isCopying && shouldCopy) {
        isCopying = true
        clipboardManager.setText(
            AnnotatedString(list.code)
        )
        snackbarController.showDismissibleSnackbar("Copied code to clipboard")
    } else if (isCopying) {
        isCopying = false
    }

    Column(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "If you want to share this $type use this code or scan the QR Code"
        )

        OutlinedTextField(
            value = list.code,
            onValueChange = {},
            readOnly = true,
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            interactionSource = interactionSource,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .width(300.dp),
            trailingIcon = {
                Icon(imageVector = Icons.Filled.ContentCopy, contentDescription = "copy code")
            }
        )

        Box(
            modifier = Modifier
                .height(300.dp)
                .width(300.dp)
        ) {
            CoilImage(
                data = QRCodeUtils.generateQRCodeUrl(list.code),
                contentDescription = list.code,
                fadeIn = true,
                contentScale = ContentScale.FillBounds,
                error = {
                    Image(
                        painter = painterResource(id = R.drawable.default_image),
                        contentDescription = list.code,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                },
                loading = {
                    AnimatedShimmerLoading()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}