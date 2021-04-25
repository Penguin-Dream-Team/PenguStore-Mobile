package store.pengu.mobile.views.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.partials.ItemCard
import store.pengu.mobile.views.partials.SearchTopBar

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun SearchScreen(navController: NavHostController, productsService: ProductsService, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val openDialog = remember { mutableStateOf(false) }
    val selectedProductId = remember { mutableStateOf(-2L) }
    val selectedPantryId = remember { mutableStateOf(-2L) }
    val alertDialogView = remember { mutableStateOf(0) }
    val amountAvailable = remember { mutableStateOf(0) }
    val amountNeeded = remember { mutableStateOf(0) }

    productsService.getProducts()

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
    ) {
        SearchTopBar()

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            modifier = Modifier
                .padding(horizontal = 7.dp),
            state = rememberLazyListState()
        ) {
            items(storeState.products) { product ->
                ItemCard(
                    name = product.name,
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 7.dp)
                        .clickable(onClickLabel = "Add to pantry"
                ) {
                    storeState.selectedProduct = product
                    selectedProductId.value = product.id
                    alertDialogView.value = 0
                    amountAvailable.value = 0
                    amountNeeded.value = 0
                    selectedPantryId.value = -2L
                    //openDialog.value = true
                    navController.navigate("product")
                })
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
                    Text(text = "Add to Pantry")
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
                        // TODO add to new pantry
                        /*item {
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
                        }*/
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
                            /*if (selectedPantryId.value == -1L) {
                                alertDialogView.value = 0
                                openDialog.value = false

                            } else {*/
                                productsService.addProduct(
                                    selectedPantryId.value,
                                    selectedProductId.value,
                                    amountAvailable.value,
                                    amountNeeded.value
                                )
                                alertDialogView.value = 0
                                openDialog.value = false
                            //}
                        }) {
                        Text("Add Product")
                    }
                }
            )
        }
    }
}