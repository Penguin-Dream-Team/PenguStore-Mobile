package store.pengu.mobile.views.lists.partials

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.data.ProductInShoppingList
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.Math

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShoppingList(navController: NavController, productsService: ProductsService, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val openDialog = remember { mutableStateOf(false) }
    val selectedShoppingList = storeState.selectedList as ShoppingList?
    val products by remember { mutableStateOf(store.shoppingListProducts) }
    val cartProducts by remember { mutableStateOf(store.cartProducts) }
    val desiredAmount = remember { mutableStateOf(1) }
    val currentProduct = remember { mutableStateOf(ProductInShoppingList(0L, 0L, "", "", 0, 2, 4.20)) }
    val queueTime = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val refreshQueueTime = coroutineScope.launch {
        queueTime.value = productsService.timeQueue()
    }

    if (selectedShoppingList == null) return
    productsService.getShoppingListProducts(selectedShoppingList.id)
    refreshQueueTime.start()

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            selectedShoppingList.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.queue_time),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = { refreshQueueTime.start() },
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "Refresh Queue Time"
                )
            }
        }

        Text(
            Math.secondsToMinutes(queueTime.value),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

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
                    if (cartProducts.map { it.first }.contains(product)) {
                        IconButton(
                            onClick = {
                                storeState.cartProducts.removeAt(cartProducts.map { it.first }.indexOf(product))
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RemoveShoppingCart,
                                tint = Color(52, 247, 133),
                                contentDescription = "Remove from Cart"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                if ((product.amountNeeded - product.amountAvailable) == 1) {
                                    storeState.cartProducts.add(Pair(product, 1))
                                } else {
                                    currentProduct.value = product
                                    desiredAmount.value = 0
                                    openDialog.value = true
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddShoppingCart,
                                tint = Color(52, 247, 133),
                                contentDescription = "Add to Cart"
                            )
                        }
                    }
                }
            }
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