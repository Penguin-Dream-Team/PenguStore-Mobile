package store.pengu.mobile.views.products.ProductInfo.bottommenu

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.ImageLoader
import com.google.accompanist.coil.LocalImageLoader
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import io.ktor.util.*
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.data.Product
import store.pengu.mobile.utils.ImageUtils
import store.pengu.mobile.views.partials.AnimatedShimmerLoading
import store.pengu.mobile.views.partials.IconButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ProductFormBottomSheetMenu(
    imageLoader: ImageLoader,
    product: Product,
    closeMenu: () -> Unit,
    onSave: suspend (String, String?, String?) -> Unit,
    onEditLists: () -> Unit,
) {
    var image by remember { mutableStateOf(null as Uri?) }
    var productImage by remember { mutableStateOf("") }
    var canUploadImage by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val permissionToUseCamera = stringResource(R.string.permission_to_use_camera_not_granted)
    val validBarcodeNotFound = stringResource(R.string.valid_barcode_not_found)
    val chooseImageForProduct = stringResource(R.string.choose_image_for_product)

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            with(it) {
                if (resultCode == RESULT_OK) {
                    productImage = if (data != null && data?.data != null) {
                        data?.data.toString()
                    } else {
                        MediaScannerConnection.scanFile(
                            context,
                            arrayOf(image.toString()),
                            null,
                            null
                        )
                        image.toString()
                    }
                }
            }
        }


    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        val imagePainter = rememberCoilPainter(
            request = productImage,
            fadeIn = true,
        )
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = FocusRequester()
        val coroutineScope = rememberCoroutineScope()

        var productName by remember { mutableStateOf(product.name) }
        var productBarcode by remember { mutableStateOf(product.barcode) }

        var loading by remember { mutableStateOf(false) }
        val canSave =
            !loading && productName.isNotBlank() && (productName != product.name || productBarcode != product.barcode || productImage.isNotBlank())

        val save: () -> Unit = {
            loading = true
            keyboardController?.hide()
            coroutineScope.launch {
                if (canSave) {
                    if (productBarcode.isNullOrBlank()) {
                        productBarcode = null
                    }
                    val imageData = ImageUtils.getEncodedImage(context, productImage)
                    onSave(
                        productName,
                        productBarcode,
                        productImage.let { if (it.isNotBlank()) imageData else null })
                    productImage = ""
                    loading = false
                    closeMenu()
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 15.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { closeMenu() },
                    icon = Icons.Filled.ArrowBack,
                    description = "close edit popup"
                )
                Text(
                    text = stringResource(R.string.edit_product),
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1.0f, true))
                IconButton(
                    onClick = {
                        productName = product.name
                        productBarcode = product.barcode
                    },
                    icon = Icons.Filled.Delete,
                    description = "clear edit data",
                    enabled = canSave
                )
            }

            Divider(modifier = Modifier.padding(top = 2.dp))

            OutlinedTextField(
                value = productName,
                onValueChange = {
                    productName = it
                },
                placeholder = {
                    Text(stringResource(R.string.product_name))
                },
                enabled = !loading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    keyboardController?.hide()
                    focusRequester.requestFocus()
                }),
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Label, contentDescription = "product name")
                },
                trailingIcon = {
                    if (productName != product.name) {
                        IconButton(
                            onClick = {
                                productName = product.name
                            },
                            icon = Icons.Filled.Clear,
                            description = "Clear"
                        )
                    }
                },
                isError = productName.isBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
            )

            OutlinedTextField(
                value = productBarcode ?: "",
                onValueChange = {
                    productBarcode = it
                },
                placeholder = {
                    Text(stringResource(R.string.product_barcode))
                },
                enabled = !loading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    save()
                }),
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.QrCode, contentDescription = "product barcode")
                },
                trailingIcon = {
                    if (productBarcode != product.barcode) {
                        IconButton(
                            onClick = {
                                productBarcode = product.barcode
                            },
                            icon = Icons.Filled.Clear,
                            description = "Clear"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
                    .focusRequester(focusRequester)
            )

            AnimatedVisibility(
                visible = productImage.isNotBlank(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 15.dp)
                        .clip(RoundedCornerShape(5))
                ) {
                    Image(
                        painter = imagePainter,
                        contentDescription = "product image",
                        contentScale = ContentScale.Crop
                    )
                    when (imagePainter.loadState) {
                        is ImageLoadState.Loading -> AnimatedShimmerLoading()
                        is ImageLoadState.Error -> Image(
                            painterResource(R.drawable.default_image),
                            contentDescription = "product image",
                            contentScale = ContentScale.Crop
                        )
                        else -> Unit
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 35.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val outputPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        val chooser = Intent.createChooser(
                            Intent(Intent.ACTION_CHOOSER),
                            chooseImageForProduct
                        ).apply {
                            putExtra(Intent.EXTRA_INTENT,
                                Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" })
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            val timeStamp: String =
                                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                            val photoURI = File.createTempFile(
                                "JPEG_${timeStamp}_",
                                ".jpg",
                                outputPath
                            ).run {
                                FileProvider.getUriForFile(
                                    context,
                                    "store.pengu.mobile.fileprovider",
                                    this
                                )
                            }
                            Log.d("Hello", photoURI.toString())
                            image = photoURI
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
                        }
                        launcher.launch(chooser)
                    }, enabled = canUploadImage,
                    modifier = Modifier
                        .weight(0.5f)
                ) {
                    Icon(imageVector = Icons.Filled.Image, contentDescription = "choose image")
                    Text(
                        text = (if (productImage.isBlank()) stringResource(R.string.choose) else stringResource(
                            R.string.replace
                        )) + stringResource(R.string.space_image)
                    )
                }
                AnimatedVisibility(
                    visible = productImage.isNotBlank(),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .weight(0.5f)
                ) {
                    Button(
                        onClick = {
                            productImage = ""
                        },
                        enabled = canUploadImage && productImage.isNotBlank(),
                    ) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "clear image")
                        Text(text = stringResource(R.string.clear_image))
                    }
                }
            }

            Button(
                onClick = save, enabled = canSave,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.save_changes))
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier
                        .weight(0.5f, true)
                )
                Text(
                    text = stringResource(R.string.or),
                    modifier = Modifier
                        .weight(0.2f, true),
                    textAlign = TextAlign.Center
                )
                Divider(
                    modifier = Modifier
                        .weight(0.5f, true)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Button(
                    onClick = onEditLists,
                    modifier = Modifier
                        .weight(0.5f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = "edit lists",
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(text = stringResource(R.string.edit_lists))
                }
            }
        }
    }
}
