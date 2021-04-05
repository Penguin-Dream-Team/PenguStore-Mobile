package store.pengu.mobile.views.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState

@Composable
fun SearchScreen(navController: NavController, productsService: ProductsService, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val openDialog = remember { mutableStateOf(false) }
    val selectedProductId = remember { mutableStateOf(-2L) }
    val selectedPantryId = remember { mutableStateOf(-2L) }
    val alertDialogView = remember { mutableStateOf(0) }
    val amountAvailable = remember { mutableStateOf(0) }
    val amountNeeded = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(storeState.products) { product ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .background(MaterialTheme.colors.secondaryVariant),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.SemiBold,
                    )
                    IconButton(
                        onClick = {
                            selectedProductId.value = product.productId
                            alertDialogView.value = 0
                            amountAvailable.value = 0
                            amountNeeded.value = 0
                            selectedPantryId.value = -2L
                            openDialog.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add to a pantry"
                        )
                    }
                }
            }
        }
    }

    if (openDialog.value) {
        if (alertDialogView.value == 0) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
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
                            openDialog.value = false
                        }) {
                        Text("Cancel")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            alertDialogView.value = 1
                        }) {
                        Text("Next")
                    }
                }
            )
        } else {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = "Select a pantry to add the item")
                },
                text = {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(storeState.pantryLists) { pantry ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = pantry.name,
                                    fontWeight = FontWeight.SemiBold
                                )
                                RadioButton(
                                    selected = selectedPantryId.value == pantry.id,
                                    onClick = { selectedPantryId.value = pantry.id }
                                )
                            }
                        }

                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Create a new Pantry",
                                    fontWeight = FontWeight.SemiBold
                                )
                                RadioButton(
                                    selected = selectedPantryId.value == -1L,
                                    onClick = { selectedPantryId.value = -1L }
                                )
                            }
                        }
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            alertDialogView.value = 0
                        }) {
                        Text("Back")
                    }
                },
                confirmButton = {
                    Button(
                        enabled = selectedPantryId.value != -2L,
                        onClick = {
                            if (selectedPantryId.value == -1L) {
                                alertDialogView.value = 0
                                openDialog.value = false
                                storeState.selectedProduct = selectedProductId.value
                                storeState.amountAvailable = amountAvailable.value
                                storeState.amountNeeded = amountNeeded.value
                                navController.navigate("new_pantry")
                            } else {
                                productsService.addProduct(
                                    selectedPantryId.value,
                                    selectedProductId.value,
                                    amountAvailable.value,
                                    amountNeeded.value
                                )
                                alertDialogView.value = 0
                                openDialog.value = false
                            }
                        }) {
                        Text("Add Product")
                    }
                }
            )
        }
    }
}