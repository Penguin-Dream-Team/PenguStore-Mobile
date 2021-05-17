@file:Suppress("PackageName")

package store.pengu.mobile.views.products.AddProductToListView

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.pengu.mobile.R
import store.pengu.mobile.data.productlists.ProductShoppingListEntry

@ExperimentalAnimationApi
@Composable
fun ShoppingListDialog(
    listName: String,
    product: ProductShoppingListEntry?,
    price: Double?,
    setPrice: (Double?) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit,
) {
    var enabled by remember { mutableStateOf(true) }
    val close = {
        onClose()
    }

    val canSave = product?.price != price && price != null && enabled

    val save = {
        if (canSave) {
            enabled = false
            onSave()
        }
    }

    if (product != null) {
        AlertDialog(
            onDismissRequest = {
                close()
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = listName,
                            modifier = Modifier.padding(start = 12.dp),
                            fontSize = 18.sp
                        )
                    }

                    Divider()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        var text by remember { mutableStateOf(price?.toString() ?: "") }
                        OutlinedTextField(
                            placeholder = { Text(stringResource(R.string.price)) },
                            value = text,
                            onValueChange = {
                                setPrice(it.toDoubleOrNull())
                                text = it
                                text = it
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.EuroSymbol,
                                    contentDescription = "price"
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                save()
                            }),
                            singleLine = true,
                            isError = price == null,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        close()
                    }) {
                    Text(stringResource(R.string.close))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        save()
                    },
                    enabled = canSave
                ) {
                    Text(stringResource(R.string.save))
                }
            },
        )
    }
}
