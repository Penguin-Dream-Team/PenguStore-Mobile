@file:Suppress("PackageName")

package store.pengu.mobile.views.products.AddProductToListView

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import io.ktor.util.*
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.Product
import store.pengu.mobile.data.productlists.ProductListEntry
import store.pengu.mobile.data.productlists.ProductPantryListEntry
import store.pengu.mobile.data.productlists.ProductShoppingListEntry
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.partials.IconButton
import store.pengu.mobile.views.partials.pulltorefresh.LoadingProgressIndicator
import store.pengu.mobile.views.products.partials.Header
import store.pengu.mobile.views.products.partials.PantryListCard
import store.pengu.mobile.views.products.partials.ShoppingListCard
import store.pengu.mobile.views.products.partials.Suggestions

/**
 * Step 2:
 *  Choose pantries:
 *      - have quantity
 *      - want quantity
 *  Choose Shopping Lists:
 *      - price?
 */
@SuppressLint("RestrictedApi")
@KtorExperimentalAPI
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun EditProductListsView(
    imageLoader: ImageLoader,
    snackbarController: SnackbarController,
    listsService: ListsService,
    productsService: ProductsService,
    store: StoreState,
    listType: UserListType = UserListType.PANTRY,
    productId: Long,
    navController: NavHostController,
    listId: Long? = null,
) {
    var selectedTab by remember { mutableStateOf(listType.ordinal) }
    store.selectedListType.value = selectedTab
    val pantryLists = productsService.getProductPantryLists(productId)
    val shoppingLists = productsService.getProductShoppingLists(productId)
    var loading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var selectedList by remember { mutableStateOf(null as ProductListEntry?) }
    var addToList by remember { mutableStateOf(listId != null) }
    var showPantryDialog by remember { mutableStateOf(false) }
    var showShoppingListDialog by remember { mutableStateOf(false) }

    val (showSuggestion, setShowSuggestion) = remember { mutableStateOf(false) }
    var suggestion: Product? by remember{ mutableStateOf(null) }

    AnimatedVisibility(visible = loading) {
        coroutineScope.launch {
            productsService.fetchProduct(productId)

            if (addToList) {
                val list = listsService.getList(listType, listId!!)
                list?.let {
                    when (listType) {
                        UserListType.PANTRY -> {
                            selectedList = ProductPantryListEntry(
                                listId = listId,
                                listName = list.name,
                                color = list.color,
                                amountAvailable = 0,
                                amountNeeded = 0,
                                isShared = list.shared,
                                latitude = list.latitude,
                                longitude = list.longitude
                            )
                            //showPantryDialog = true
                        }
                        UserListType.SHOPPING_LIST -> {
                            selectedList = ProductShoppingListEntry(
                                listId = listId,
                                listName = list.name,
                                color = list.color,
                                price = 0.0,
                                latitude = list.latitude,
                                longitude = list.longitude
                            )
                            //showShoppingListDialog = true
                        }
                    }
                }
            }

            loading = false
        }

        if (selectedList != null) {
            when (listType) {
                UserListType.PANTRY -> showPantryDialog = true
                UserListType.SHOPPING_LIST -> showShoppingListDialog = true
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoadingProgressIndicator(
                progressColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.surface
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = stringResource(R.string.loading)
            )
        }
    }

    if (!loading) {
        if (store.selectedProduct == null) {
            navController.popBackStack()
            return
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    stringResource(R.string.edit_product_lists),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = { loading = true },
                    icon = Icons.Filled.Refresh,
                    description = "Refresh"
                )
            }

            Header(imageLoader, store.selectedProduct!!)

            TabRow(
                selectedTabIndex = selectedTab,
                contentColor = MaterialTheme.colors.primary,
            ) {
                Tab(
                    modifier = Modifier.padding(top = 15.dp, bottom = 13.dp),
                    selected = selectedTab == 0,
                    content = { Text(stringResource(R.string.pantries)) },
                    onClick = { selectedTab = 0 },
                    unselectedContentColor = MaterialTheme.colors.onSurface
                )
                Tab(
                    modifier = Modifier.padding(top = 15.dp, bottom = 13.dp),
                    selected = selectedTab == 1,
                    content = { Text(stringResource(R.string.shopping)) },
                    onClick = { selectedTab = 1 },
                    unselectedContentColor = MaterialTheme.colors.onSurface
                )
            }

            ListsTab(visible = selectedTab == 0, lists = pantryLists, onRefresh = {
                productsService.fetchProductPantryLists(productId)
            }) { item, enabled ->
                PantryListCard(item, enabled) {
                    selectedList = item
                    showPantryDialog = true
                }
            }

            ListsTab(
                visible = selectedTab == 1,
                lists = shoppingLists,
                onRefresh = {
                    productsService.fetchProductShoppingLists(productId)
                }) { item, enabled ->
                ShoppingListCard(item, enabled) {
                    selectedList = item
                    showShoppingListDialog = true
                }
            }
        }

        if (showPantryDialog && selectedList != null) {
            (selectedList as ProductPantryListEntry).let {
                val (haveAmount, setHaveAmount) = remember { mutableStateOf(it.amountAvailable) }
                val (needAmount, setNeedAmount) = remember { mutableStateOf(it.amountNeeded) }
                PantryListDialog(
                    it.listName,
                    product = store.selectedProduct!!.toProductInPantry(
                        it.listId,
                        it.amountAvailable,
                        it.amountNeeded
                    ),
                    haveAmount = haveAmount,
                    needAmount = needAmount,
                    setHaveAmount = setHaveAmount,
                    setNeedAmount = setNeedAmount,
                    onClose = {
                        showPantryDialog = false
                        selectedList = null
                        navController.popBackStack()
                    },
                    onSave = {
                        coroutineScope.launch {
                            suggestion = productsService.addProductToPantryList(
                                productId,
                                store.selectedProduct?.barcode,
                                it.listId,
                                haveAmount,
                                needAmount
                            )
                            showPantryDialog = false
                            setShowSuggestion(true)
                            selectedList = null
                            if (addToList) {
                                navController.popBackStack()
                            }
                        }
                    }
                )
            }
        }

        if (showShoppingListDialog && selectedList != null) {
            (selectedList as ProductShoppingListEntry).let {
                val (price, setPrice) = remember { mutableStateOf(it.price) }
                ShoppingListDialog(
                    it.listName,
                    product = it,
                    price = price,
                    setPrice = setPrice,
                    onClose = {
                        showShoppingListDialog = false
                        selectedList = null
                        navController.popBackStack()
                    },
                    onSave = {
                        coroutineScope.launch {
                            productsService.addProductToShoppingList(
                                productId,
                                it.listId,
                                price ?: 0.0
                            )
                            showShoppingListDialog = false
                            selectedList = null
                            if (addToList) {
                                navController.popBackStack()
                            }
                        }
                    }
                )
            }
        }
    }

    if (showSuggestion) {
        if (suggestion != null && selectedList != null)
            Suggestions(navController, suggestion!!, selectedList!!.listId, setShowSuggestion)
        else setShowSuggestion(false)
    }
}
