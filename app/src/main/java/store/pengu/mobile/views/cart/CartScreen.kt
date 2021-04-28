package store.pengu.mobile.views.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.data.ProductInShoppingList
import store.pengu.mobile.states.StoreState

@Composable
fun CartScreen(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val openDialog = remember { mutableStateOf(false) }
    val products by remember { mutableStateOf(store.cartProducts) }
    val desiredAmount = remember { mutableStateOf(1) }
    val currentProduct = remember { mutableStateOf(ProductInShoppingList(0L, 0L, "", "", 0, 2, 4.20)) }
    store.numItems = 0

    Column(verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cart", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))

        if (products.isEmpty()) {
            Text(text = "Your cart is empty")
        } else {
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
                            text = "${product.first.name}: ${product.second}",
                            fontWeight = FontWeight.SemiBold
                        )

                        if ((product.first.amountNeeded - product.first.amountAvailable) != 1) {
                            IconButton(
                                onClick = {
                                    currentProduct.value = product.first
                                    desiredAmount.value = product.second
                                    openDialog.value = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    tint = Color(52, 247, 133),
                                    contentDescription = "Edit"
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                storeState.cartProducts.removeAt(products.indexOf(product))
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RemoveCircle,
                                tint = Color(52, 247, 133),
                                contentDescription = "Remove"
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                products.forEach { product ->
                    storeState.numItems = storeState.numItems!!.plus(product.second)
                }
                navController.navigate("cart_confirmation")
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Checkout",
                textAlign = TextAlign.Center
            )
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Select the desired amount")
            },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (desiredAmount.value > 1)
                                desiredAmount.value--
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.RemoveCircle,
                            contentDescription = "Remove"
                        )
                    }

                    Text(text = "Amount: ${desiredAmount.value}")

                    IconButton(
                        onClick = {
                            if (desiredAmount.value < (currentProduct.value.amountNeeded - currentProduct.value.amountAvailable))
                                desiredAmount.value++
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add"
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        storeState.cartProducts.add(Pair(currentProduct.value, desiredAmount.value))
                    }) {
                    Text("Add to Cart")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("Close")
                }
            }
        )
    }
}