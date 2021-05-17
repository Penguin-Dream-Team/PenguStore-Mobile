package store.pengu.mobile.views.cart

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.MutableShopItem
import store.pengu.mobile.services.CartService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.secondsToMinutes

@ExperimentalAnimationApi
@Composable
fun CartScreen(
    snackbarController: SnackbarController,
    navController: NavController,
    cartService: CartService,
    productsService: ProductsService,
    listsService: ListsService,
    store: StoreState
) {
    var (queueTime, setQueueTime) = remember { mutableStateOf(null as Int?) }
    val context = LocalContext.current
    var canFinish by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
        ) {
            Text(text = stringResource(R.string.cart), fontWeight = FontWeight.Bold)
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
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
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (store.cartProducts.isEmpty()) {
                item {
                    Text(text = stringResource(R.string.cart_empty))
                }
            } else {
                store.cartProducts.keys.forEach { list ->
                    val productsInList = mutableStateListOf<MutableShopItem>()
                    productsInList.addAll(store.cartProducts[list] ?: emptyList())
                    if (productsInList.isNotEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                                        RoundedCornerShape(5)
                                    )
                            ) {
                                Text(
                                    text = productsInList.firstOrNull()?.listName ?: "",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.offset(x = 5.dp, y = 2.dp)
                                )
                                productsInList.forEach { product ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowRight,
                                                contentDescription = "indent"
                                            )
                                            Text(
                                                text = "${product.productName}: ${product.inCart.value}",
                                            )
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            if (product.inCart.value < product.amountNeeded) {
                                                IconButton(
                                                    onClick = {
                                                        product.inCart.value++
                                                    },
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.AddCircle,
                                                        tint = Color(52, 247, 133),
                                                        contentDescription = "Add",
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }

                                            IconButton(
                                                onClick = {
                                                    product.inCart.value--
                                                    store.cartProducts[list]?.removeIf { it.inCart.value <= 0 }
                                                },
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.RemoveCircle,
                                                    tint = Color.Red,
                                                    contentDescription = "Remove",
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            enabled = canFinish && store.cartProducts.isNotEmpty(),
            onClick = {
                canFinish = false
                coroutineScope.launch {
                    try {
                        cartService.buyCart()
                        navController.navigate("lists")
                    } catch (e: Exception) {
                        snackbarController.showDismissibleSnackbar(e.message ?: "Oops")
                    }
                    canFinish = true
                }
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
}