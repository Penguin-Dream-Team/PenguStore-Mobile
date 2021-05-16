package store.pengu.mobile.views.lists.shops

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.pengu.mobile.data.ListProduct
import store.pengu.mobile.data.MutableShopItem
import store.pengu.mobile.views.partials.IconButton

@ExperimentalAnimationApi
@Composable
fun ShopProductItemDialog(
    product: ListProduct?,
    pantries: SnapshotStateMap<Long, MutableShopItem>,
    onClose: () -> Unit,
    onSave: () -> Unit,
    onViewInfo: () -> Unit,
) {
    val close = {
        onClose()
    }
    var canSave by remember { mutableStateOf(false) }
    var canFill by remember { mutableStateOf(true) }

    if (product != null) {
        canSave = pantries.values.any { it.inCart.value != 0 }
        canFill = pantries.values.any { it.inCart.value != it.amountNeeded }

        AlertDialog(
            onDismissRequest = {
                close()
            },
            modifier = Modifier.heightIn(max = 400.dp),
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = product.name,
                            modifier = Modifier.padding(start = 12.dp),
                            fontSize = 18.sp
                        )
                        IconButton(
                            onClick = onViewInfo,
                            icon = Icons.Filled.ArrowRight,
                            description = "View product info"
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
                        Button(
                            onClick = {
                                pantries.values.forEach { it.inCart.value = 0 }
                            },
                            enabled = canSave,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RemoveShoppingCart,
                                contentDescription = "Clear"
                            )
                            Text("Reset")
                        }
                        Button(
                            onClick = {
                                pantries.values.forEach { it.inCart.value = it.amountNeeded }
                            },
                            enabled = canFill,
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(start = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddShoppingCart,
                                contentDescription = "Fill"
                            )
                            Text("Fill")
                        }
                    }
                    Divider()
                    Divider(modifier = Modifier.padding(bottom = 5.dp))

                    pantries.values.forEach { pantry ->
                        Text(
                            text = pantry.listName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                        Divider()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 3.dp, bottom = 8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = { pantry.inCart.value-- },
                                enabled = pantry.inCart.value > 0,
                                icon = Icons.Filled.RemoveCircle,
                                description = "Decrement amount needed"
                            )

                            Text(text = "Amount Needed: ${pantry.inCart.value} / ${pantry.amountNeeded}")

                            IconButton(
                                onClick = { pantry.inCart.value++ },
                                enabled = pantry.inCart.value < pantry.amountNeeded,
                                icon = Icons.Filled.AddCircle,
                                description = "Increment amount needed"
                            )
                        }
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        close()
                    }) {
                    Text("Close")
                }
            },
            confirmButton = {
                Button(
                    onClick = onSave,
                    enabled = canSave
                ) {
                    Text("Save")
                }
            },
        )
    }
}
