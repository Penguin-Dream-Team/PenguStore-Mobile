package store.pengu.mobile.views.lists.pantry

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState

@ExperimentalAnimationApi
@Composable
fun ViewPantryList(
    navController: NavController,
    productsService: ProductsService,
    store: StoreState,
    pantryList: PantryList
) {
    val openDialogEdit = remember { mutableStateOf(false) }
    val openDialogDelete = remember { mutableStateOf(false) }
    val selectedProductId = remember { mutableStateOf(-1L) }
    val products by remember { mutableStateOf(store.pantryProducts) }
    val amountAvailable = remember { mutableStateOf(0) }
    val amountNeeded = remember { mutableStateOf(0) }

    productsService.getPantryProducts(pantryList.id)

    Spacer(modifier = Modifier.height(32.dp))

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(products) { product ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${product.name}: ${product.amountAvailable} out of ${product.amountNeeded}",
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(
                    onClick = {
                        selectedProductId.value = product.productId
                        amountAvailable.value = product.amountAvailable
                        amountNeeded.value = product.amountNeeded

                        openDialogEdit.value = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        tint = Color(52, 247, 133),
                        contentDescription = "Update Product"
                    )
                }

                IconButton(
                    onClick = {
                        selectedProductId.value = product.productId
                        openDialogDelete.value = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        tint = Color(52, 247, 133),
                        contentDescription = "Delete Product"
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = {
            navController.navigate("search")
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Add new Item",
            textAlign = TextAlign.Center
        )
    }

    if (openDialogEdit.value) {
        AlertDialog(
            onDismissRequest = {
                openDialogEdit.value = false
            },
            title = {
                Text(text = "Select the amount available and desired")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                if (amountAvailable.value > 0)
                                    amountAvailable.value--
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RemoveCircle,
                                contentDescription = "Remove"
                            )
                        }

                        Text(text = "Amount Available: ${amountAvailable.value}")

                        IconButton(
                            onClick = {
                                amountAvailable.value++
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Add"
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                if (amountNeeded.value > 0)
                                    amountNeeded.value--
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RemoveCircle,
                                contentDescription = "Remove"
                            )
                        }

                        Text(text = "Amount Needed: ${amountNeeded.value}")

                        IconButton(
                            onClick = {
                                amountNeeded.value++
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Add"
                            )
                        }
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialogEdit.value = false
                    }) {
                    Text("Cancel")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialogEdit.value = false
                        productsService.updateProduct(
                            pantryList.id,
                            selectedProductId.value,
                            amountAvailable.value,
                            amountNeeded.value
                        )
                    }) {
                    Text("Edit Product")
                }
            }
        )
    }

    if (openDialogDelete.value) {
        AlertDialog(
            onDismissRequest = {
                openDialogDelete.value = false
            },
            title = {
                Text(text = "You sure you want you remove this product??")
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialogDelete.value = false
                    }) {
                    Text("Cancel")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialogDelete.value = false
                        productsService.deleteProduct(
                            pantryList.id,
                            selectedProductId.value
                        )
                    }) {
                    Text("Confirm")
                }
            }
        )
    }
}