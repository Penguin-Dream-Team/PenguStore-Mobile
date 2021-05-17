package store.pengu.mobile.views.cart

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.delay
import store.pengu.mobile.R
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.MutableShopItem
import store.pengu.mobile.services.CartService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.secondsToMinutes

@ExperimentalAnimationApi
@Composable
fun CartScreen(
    navController: NavController,
    cartService: CartService,
    productsService: ProductsService,
    listsService: ListsService,
    store: StoreState
) {
    val openDialog = remember { mutableStateOf(false) }
    var currentProduct: MutableShopItem? = null
    var (queueTime, setQueueTime) = remember { mutableStateOf(null as Int?) }
    val context = LocalContext.current

    LaunchedEffect(null) {
        while (true) {
            try {
                setQueueTime(
                    productsService.getQueueTime(
                        listsService.getList(
                            UserListType.SHOPPING_LIST,
                            store.cartShoppingList!!
                        )!!.location
                    )
                )
            } catch (e: Exception) {
                queueTime = null
            }
            delay(5000)
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cart", fontWeight = FontWeight.Bold)
        AnimatedVisibility(visible = queueTime != null) {
            Divider()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .alpha(0.7f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.queueTimeLabel),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(end = 3.dp),
                        maxLines = 1
                    )
                    Text(
                        text = queueTime!!.secondsToMinutes(context),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(start = 3.dp),
                        maxLines = 1
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(32.dp))

        if (store.cartProducts.isEmpty()) {
            Text(text = "Your cart is empty")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                store.cartProducts.keys.forEach { list ->
                    val productsInList =
                        mutableStateOf(store.cartProducts[list]?.toList() ?: emptyList())
                    items(productsInList.value) { product ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = product.productName + stringResource(R.string.in_pantry) + product.listName + ": " + product.inCart.value,
                                fontWeight = FontWeight.SemiBold
                            )

                            if ((product.amountNeeded - product.amountAvailable) != 1) {
                                IconButton(
                                    onClick = {
                                        currentProduct = product
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
                                    store.cartProducts[list]?.let {
                                        it.remove(product)
                                        if (it.isEmpty()) store.cartShoppingList = null
                                    }
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
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                cartService.buyCart()
                navController.navigate("lists")
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.finish_shopping),
                textAlign = TextAlign.Center
            )
        }
    }

    if (openDialog.value && currentProduct != null) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = stringResource(R.string.select_desired_amount))
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
                            if (currentProduct!!.inCart.value > 1)
                                currentProduct!!.inCart.value--
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.RemoveCircle,
                            contentDescription = "Remove"
                        )
                    }

                    Text(text = stringResource(R.string.amount) + currentProduct!!.inCart.value)

                    IconButton(
                        onClick = {
                            if (currentProduct!!.inCart.value < (currentProduct!!.amountNeeded - currentProduct!!.amountAvailable))
                                currentProduct!!.inCart.value++
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
                    }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }
}