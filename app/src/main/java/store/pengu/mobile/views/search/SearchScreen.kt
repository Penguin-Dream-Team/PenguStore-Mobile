package store.pengu.mobile.views.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.partials.ItemCard
import store.pengu.mobile.views.partials.SearchTopBar
import store.pengu.mobile.views.partials.pulltorefresh.PullToRefresh

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun SearchScreen(
    navController: NavHostController,
    productsService: ProductsService,
    store: StoreState
) {
    val storeState by remember { mutableStateOf(store) }
    val selectedProductId = remember { mutableStateOf(-2L) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
    ) {
        SearchTopBar()

        Spacer(modifier = Modifier.height(32.dp))

        var isRefreshing: Boolean by remember { mutableStateOf(false) }
        var needsRefresh: Boolean by remember { mutableStateOf(true) }
        val refresh = {
            isRefreshing = true
            coroutineScope.launch(Dispatchers.IO) {
                productsService.fetchAllProducts()
                isRefreshing = false
            }
        }

        if (needsRefresh && !isRefreshing) {
            refresh()
            needsRefresh = false
        }

        PullToRefresh(
            isRefreshing = isRefreshing,
            onRefresh = {
                refresh()
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(horizontal = 7.dp),
                state = rememberLazyListState()
            ) {
                items(productsService.getAllProducts()) { product ->
                    ItemCard(
                        name = product.name,
                        image = product.image,
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 7.dp)
                            .clickable(
                                onClickLabel = "Add to pantry"
                            ) {
                                storeState.selectedProduct = product
                                selectedProductId.value = product.id
                                navController.navigate("product/${product.id}")
                            })
                }
            }
        }
    }
}