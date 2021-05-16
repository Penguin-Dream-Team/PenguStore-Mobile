package store.pengu.mobile.views.products.ProductInfo

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.utils.Histogram

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ProductRatings(
    productsService: ProductsService,
    productId: Long,
    ratings: List<Int>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 15.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = "Ratings",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Divider(modifier = Modifier.padding(top = 3.dp, bottom = 5.dp))

        Histogram(ratings)
    }
}
