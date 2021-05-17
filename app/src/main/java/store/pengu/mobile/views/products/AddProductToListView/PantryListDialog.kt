@file:Suppress("PackageName")

package store.pengu.mobile.views.products.AddProductToListView

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.pengu.mobile.R
import store.pengu.mobile.data.ListProduct
import store.pengu.mobile.views.partials.IconButton

@ExperimentalAnimationApi
@Composable
fun PantryListDialog(
    listName: String,
    product: ListProduct?,
    haveAmount: Int,
    needAmount: Int,
    setHaveAmount: (Int) -> Unit,
    setNeedAmount: (Int) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit
) {
    var enabled by remember { mutableStateOf(true) }
    val close = {
        onClose()
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
                        IconButton(
                            onClick = { setHaveAmount(haveAmount - 1) },
                            enabled = haveAmount > 0,
                            icon = Icons.Filled.RemoveCircle,
                            description = "Decrement amount available"
                        )

                        Text(text = stringResource(R.string.amount_available) + haveAmount)

                        IconButton(
                            onClick = { setHaveAmount(haveAmount + 1) },
                            icon = Icons.Filled.AddCircle,
                            description = "Increment amount available"
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { setNeedAmount(needAmount - 1) },
                            enabled = needAmount > 0,
                            icon = Icons.Filled.RemoveCircle,
                            description = "Decrement amount needed"
                        )

                        Text(text = stringResource(R.string.amount_needed) + needAmount)

                        IconButton(
                            onClick = { setNeedAmount(needAmount + 1) },
                            icon = Icons.Filled.AddCircle,
                            description = "Increment amount needed"
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
                        enabled = false
                        onSave()
                    },
                    enabled = (product.amountAvailable != haveAmount || product.amountNeeded != needAmount) && enabled
                ) {
                    Text(stringResource(R.string.save))
                }
            },
        )
    }
}
