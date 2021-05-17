package store.pengu.mobile.views.products

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import io.ktor.util.*
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.CameraService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.utils.ImageUtils
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.loading.RequestCameraPermission
import store.pengu.mobile.views.partials.AnimatedShimmerLoading
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Step 1:
 *  name
 *  barcode?
 *  image?
 */
@KtorExperimentalAPI
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun NewProductView(
    snackbarController: SnackbarController,
    cameraService: CameraService,
    productsService: ProductsService,
    shopId: Long? = null,
    pantryId: Long? = null,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 32.dp, bottom = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.create_product),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        ProductForm(
            snackbarController,
            cameraService,
            productsService,
            shopId,
            pantryId,
            navController
        )
    }

    /**
     * Step 2:
     *  Choose pantries:
     *      - have quantity
     *      - want quantity
     *  Choose Shopping Lists:
     *      - price?
     */
}

/**
 * Step 1:
 *  name
 *  barcode?
 *  image?
 */
@SuppressLint("RestrictedApi")
@KtorExperimentalAPI
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
private fun ProductForm(
    snackbarController: SnackbarController,
    cameraService: CameraService,
    productsService: ProductsService,
    shopId: Long?,
    pantryId: Long?,
    navController: NavHostController
) {
    var name by remember { mutableStateOf("test") }
    var barcode by remember { mutableStateOf("") }
    var image by remember { mutableStateOf(null as Uri?) }
    var productImage by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var scanBarcode by remember { mutableStateOf(false) }
    var canScanBarcode by remember { mutableStateOf(false) }
    var canUploadImage by remember { mutableStateOf(false) }
    var canCreate by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val permissionToUseCamera = stringResource(R.string.permission_to_use_camera_not_granted)
    val valid_barcode_not_found = stringResource(R.string.valid_barcode_not_found)
    val chooseImageForProduct = stringResource(R.string.choose_image_for_product)

    val imagePainter = rememberCoilPainter(
        request = productImage,
        fadeIn = true,
    )

    canScanBarcode = name.isNotBlank()
    canUploadImage = name.isNotBlank()
    canCreate = name.isNotBlank()

    val createProduct = {
        coroutineScope.launch {
            val imageData = ImageUtils.getEncodedImage(context, productImage)
            try {
                val product = productsService.createProduct(
                    name,
                    if (barcode.isNotBlank()) barcode else null,
                    if (productImage.isNotBlank()) imageData else null
                )

                navController.popBackStack()

                when {
                    shopId == null && pantryId == null -> {
                        navController.navigate("product/${product.id}")
                    }
                    pantryId != null -> {
                        navController.navigate("add_product_to_list/${product.id}?listType=${UserListType.PANTRY.ordinal}&listId=$pantryId")
                    }
                    else -> {
                        navController.navigate("add_product_to_list/${product.id}?listType=${UserListType.SHOPPING_LIST.ordinal}&listId=$shopId")
                    }
                }

            } catch (e: PenguStoreApiException) {
                snackbarController.showDismissibleSnackbar(e.message)
            }
        }
    }

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

    OutlinedTextField(
        value = name,
        onValueChange = {
            name = it
        },
        placeholder = {
            Text(stringResource(R.string.product_name))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            keyboardController?.hide()
        }),
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Label, contentDescription = "product name")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
    )

    AnimatedVisibility(visible = scanBarcode) {
        RequestCameraPermission(
            snackbarController,
            onFail = {
                snackbarController.showDismissibleSnackbar(permissionToUseCamera)
                scanBarcode = false
            }
        ) {
            AndroidView(factory = {
                LayoutInflater.from(it).inflate(R.layout.camera_layout, null)
            }) { inflatedLayout ->
                cameraService.initCamera(
                    context,
                    lifecycleOwner,
                    inflatedLayout as PreviewView,
                    onSuccess = {
                        barcode = it
                        scanBarcode = false
                    },
                    onFail = {
                        snackbarController.showDismissibleSnackbar(valid_barcode_not_found)
                        scanBarcode = false
                    },
                    CameraService.ScanType.PRODUCT_BARCODE
                )
            }
        }
    }

    OutlinedTextField(
        value = barcode,
        onValueChange = {
            barcode = it
        },
        placeholder = {
            Text(stringResource(R.string.product_barcode))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            keyboardController?.hide()
        }),
        leadingIcon = {
            Icon(imageVector = Icons.Filled.QrCode, contentDescription = "product barcode")
        },
        trailingIcon = {
            Button(
                onClick = {
                    scanBarcode = !scanBarcode
                },
                shape = CircleShape,
                enabled = canScanBarcode,
                contentPadding = PaddingValues(5.dp),
                modifier = Modifier
                    .size(45.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = "scan barcode",
                    tint = Color.White,
                )
            }
        },
        enabled = canScanBarcode,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 35.dp)
    )

    AnimatedVisibility(visible = productImage.isNotBlank()) {
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
                    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
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
            Text(text = (if (productImage.isBlank()) stringResource(R.string.choose) else stringResource(
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
        onClick = {
            if (barcode.isBlank() || productImage.isBlank()) {
                showDialog = true
            } else {
                createProduct()
            }
        }, enabled = canCreate,
        modifier = Modifier
            .padding(bottom = 15.dp)
            .fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "create product")
        Text(text = stringResource(R.string.create_product))
    }

    val dismissAlert = {
        showDialog = false
    }

    AnimatedVisibility(visible = showDialog) {
        AlertDialog(
            onDismissRequest = {
                dismissAlert()
            },
            confirmButton = {
                Button(
                    onClick = {
                        createProduct()
                        dismissAlert()
                    }
                ) {
                    Text(stringResource(R.string.create_without_space))
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
                Text(stringResource(R.string.confirm_product_creation))
            },
            text = {
                Text(
                    stringResource(R.string.product_creating_dont_have) + if (barcode.isBlank()) {
                        if (productImage.isBlank()) {
                            stringResource(R.string.barcode_nor_image)
                        } else {
                            stringResource(R.string.a_barcode)
                        }
                    } else {
                        stringResource(R.string.an_image)
                    }
                )
            }
        )
    }
}