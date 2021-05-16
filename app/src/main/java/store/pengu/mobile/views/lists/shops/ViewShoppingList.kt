package store.pengu.mobile.views.lists.shops

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.data.MutableShopItem
import store.pengu.mobile.data.ProductInShoppingList
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.shop
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.partials.pulltorefresh.PullToRefresh

@Suppress("UNUSED_VALUE")
@ExperimentalAnimationApi
@Composable
fun ViewShoppingList(
    navController: NavController,
    snackbarController: SnackbarController,
    productsService: ProductsService,
    store: StoreState,
    shoppingList: ShoppingList
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing: Boolean by remember { mutableStateOf(false) }
    var needsRefresh: Boolean by remember { mutableStateOf(true) }
    val refresh = {
        isRefreshing = true
        coroutineScope.launch(Dispatchers.IO) {
            delay(50L)
            productsService.fetchShoppingListProducts(shoppingList.id)
            isRefreshing = false
        }
    }

    if (needsRefresh && !isRefreshing) {
        refresh()
        needsRefresh = false
    }

    val products = remember { productsService.getShoppingListProducts(shoppingList.id) }
    var selectedProduct: ProductInShoppingList? by remember { mutableStateOf(null) }
    val pantries = remember { mutableStateMapOf<Long, MutableShopItem>() }
    val (needAmount, setNeedAmount) = remember { mutableStateOf(0) }
    val string = stringResource(R.string.NotInStore)

    PullToRefresh(
        isRefreshing = isRefreshing,
        onRefresh = {
            refresh()
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 15.dp)
                .fillMaxSize()
        ) {
            if (products.isEmpty() && !isRefreshing) {
                item {
                    Text(stringResource(R.string.empty_list_info))
                }
            }
            items(items = products) { product ->
                ShopProductItem(
                    title = product.name,
                    pantryNum = product.pantries.size,
                    needAmount = product.amountNeeded,
                    color = shoppingList.color,
                    image = product.image
                ) {
                    if (store.cartShoppingList == null || store.cartShoppingList == shoppingList.id) {
                        selectedProduct = product
                        pantries.clear()
                        pantries.putAll(product.pantries.map {
                            it.listId to MutableShopItem(
                                selectedProduct!!.id,
                                it.listName,
                                selectedProduct!!.name,
                                it.amountNeeded,
                                it.amountAvailable,
                                store.cartProducts[it.listId]?.firstOrNull { product ->
                                    product.productId == selectedProduct!!.id
                                }?.inCart?: mutableStateOf(0)
                            )
                        })
                        setNeedAmount(product.amountNeeded)
                    } else {
                        snackbarController.showDismissibleSnackbar(string)
                    }
                }
            }
        }
    }

    ShopProductItemDialog(
        product = selectedProduct,
        pantries = pantries,
        onClose = { selectedProduct = null },
        onSave = {
            coroutineScope.launch(Dispatchers.IO) {
                store.cartShoppingList = shoppingList.id
                productsService.addProductToCart(pantries)
                selectedProduct = null
            }
        },
        onViewInfo = {
            selectedProduct?.let {
                store.selectedProduct = it.toProduct()
                navController.navigate("product/${it.id}")
            }
            selectedProduct = null
        }
    )
}