package store.pengu.mobile.views.products.ProductInfo

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import coil.ImageLoader
import store.pengu.mobile.R
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.partials.IconButton
import store.pengu.mobile.views.partials.pulltorefresh.LoadingProgressIndicator
import store.pengu.mobile.views.products.partials.Header

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun ProductInfo(
    imageLoader: ImageLoader,
    productsService: ProductsService,
    navController: NavHostController,
    store: StoreState,
    productId: Long,
    expandBottomSheetMenu: () -> Unit
) {
    var loading by remember { mutableStateOf(true) }
    val images = remember { productsService.getProductImages(productId) }
    val product = store.selectedProduct
    var showPopup by remember { mutableStateOf(true) }
    var popupType by remember { mutableStateOf(null as PopupType?) }

    LaunchedEffect(loading) {
        productsService.fetchProduct(productId)
        productsService.fetchProductImages(productId)
        productsService.fetchProductPantryLists(productId)
        productsService.fetchProductShoppingLists(productId)

        when {
            product?.barcode.isNullOrBlank() -> {
                showPopup = true
                popupType = PopupType.NEED_BARCODE
            }
            productsService.getProductPantryLists(productId).isEmpty() -> {
                showPopup = true
                popupType = PopupType.NEED_PANTRY
            }
            productsService.getProductShoppingLists(productId).isEmpty() -> {
                showPopup = true
                popupType = PopupType.NEED_SHOP
            }
            images.isEmpty() -> {
                showPopup = true
                popupType = PopupType.NEED_IMAGE
            }
        }

        loading = false
    }
    if (loading) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
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
    } else {
        if (product == null) {
            navController.popBackStack()
            return
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    "View Product Info",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = { loading = !loading },
                    icon = Icons.Filled.Refresh,
                    description = "Refresh"
                )
            }

            Header(imageLoader, product)

            Spacer(modifier = Modifier.height(15.dp))
            Divider()

            Column(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                ProductGallery(imageLoader, images)

                product.barcode?.let {
                    ProductRatings(
                        productsService,
                        productId,
                        product.ratings,
                        product.userRating
                    )
                }
            }
        }
    }

    if (showPopup) {
        when (popupType) {
            PopupType.NEED_SHOP,
            PopupType.NEED_PANTRY -> {
                AlertDialog(
                    onDismissRequest = {
                        showPopup = false
                        popupType = null
                    },
                    text = {
                        if (popupType == PopupType.NEED_SHOP) {
                            Text("This product is not available in any shopping list. Do you want to add to one?")
                        } else {
                            Text("This product is not used in any pantry list. Do you want to add to one?")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showPopup = false
                                popupType = null
                            }) {
                            Text("Close")
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val listType = if (popupType == PopupType.NEED_SHOP) {
                                    UserListType.SHOPPING_LIST.ordinal
                                } else {
                                    UserListType.PANTRY.ordinal
                                }
                                showPopup = false
                                popupType = null
                                navController.navigate("add_product_to_list/${productId}?listType=${listType}")
                            }
                        ) {
                            Text("Edit Lists")
                        }
                    }
                )
            }
            PopupType.NEED_BARCODE,
            PopupType.NEED_IMAGE -> {
                AlertDialog(
                    onDismissRequest = {
                        showPopup = false
                        popupType = null
                    },
                    text = {
                        if (popupType == PopupType.NEED_BARCODE) {
                            Text("This product does not currently have a barcode. Do you want to add one?")
                        } else {
                            Text("This product does not currently have an image. Do you want to add one?")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showPopup = false
                                popupType = null
                            }) {
                            Text("Close")
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showPopup = false
                                popupType = null
                                expandBottomSheetMenu()
                            }
                        ) {
                            Text("Edit Product")
                        }
                    }
                )
            }
        }
    }

}

private enum class PopupType {
    NEED_PANTRY,
    NEED_SHOP,
    NEED_BARCODE,
    NEED_IMAGE
}