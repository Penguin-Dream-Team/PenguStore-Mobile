package store.pengu.mobile.views.products.ProductInfo.bottommenu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.ktor.util.*
import kotlinx.coroutines.launch
import store.pengu.mobile.data.Product
import store.pengu.mobile.views.partials.IconButton

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ProductFormBottomSheetMenu(
    product: Product,
    closeMenu: () -> Unit,
    onSave: suspend () -> Unit,
    onEditLists: () -> Unit,
    onUploadImage: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester()
    val coroutineScope = rememberCoroutineScope()

    var productName by remember { mutableStateOf(product.name) }
    var productBarcode by remember { mutableStateOf(product.barcode) }

    var loading by remember { mutableStateOf(false) }
    val canSave = !loading && productName.isNotBlank() && (productName != product.name || productBarcode != product.barcode)

    val save: () -> Unit = {
        loading = true
        coroutineScope.launch {
            if (canSave) {
                onSave()
                loading = false
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
                text = "Edit product",
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
                Text("Product name")
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
                Icon(imageVector = Icons.Filled.Tag, contentDescription = "product name")
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
                Text("Product barcode")
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
                .focusRequester(focusRequester)
        )

        Button(
            onClick = save, enabled = canSave,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = "Save changes")
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
                text = "or",
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
                Text(text = "Edit Lists")
            }
            Button(
                onClick = onUploadImage,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = "upload image",
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(text = "Upload Image")
            }
        }
    }
}
