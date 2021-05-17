package store.pengu.mobile.views.products.ProductInfo

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import store.pengu.mobile.R
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.partials.IconButton
import store.pengu.mobile.views.partials.pulltorefresh.LoadingProgressIndicator
import store.pengu.mobile.views.products.partials.Header

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun ProductInfo(
    productsService: ProductsService,
    navController: NavHostController,
    store: StoreState,
    productId: Long
) {
    var loading by remember { mutableStateOf(true) }
    val images = remember { productsService.getProductImages(productId) }
    val product = store.selectedProduct
    var (rating, setRating) = remember { mutableStateOf(product?.productRating ?: 0.0) }

    LaunchedEffect(loading) {
        productsService.fetchProduct(productId)
        productsService.fetchProductImages(productId)
        setRating(product?.productRating ?: 0.0)
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

            Header(product, rating)

            Spacer(modifier = Modifier.height(15.dp))
            Divider()

            Column(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                ProductGallery(images)

                product.barcode?.let {
                    ProductRatings(
                        productsService,
                        productId,
                        product.ratings,
                        product.userRating,
                        setRating
                    )
                }
            }
        }
    }
}
