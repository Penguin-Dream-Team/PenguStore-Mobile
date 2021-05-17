package store.pengu.mobile.views.products.ProductInfo

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.pengu.mobile.R
import kotlinx.coroutines.launch
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.utils.Histogram
import store.pengu.mobile.views.partials.IconButton

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ProductRatings(
    productsService: ProductsService,
    productId: Long,
    ratings: List<Int>,
    userRating: Int,
) {
    var rating by remember { mutableStateOf(userRating) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 15.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = stringResource(R.string.ratings),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Divider(modifier = Modifier.padding(top = 3.dp, bottom = 5.dp))

        if (ratings.isNotEmpty()) {
            Histogram(ratings)
        } else {
            Text(text = stringResource(R.string.no_ratings))
        }

        Text(
            text = stringResource(R.string.your_rating),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Divider(modifier = Modifier.padding(top = 3.dp, bottom = 5.dp))

        var canChangeRating by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()

        val changeRating: (Int) -> Unit = {
            if (canChangeRating) {
                canChangeRating = false
                coroutineScope.launch {
                    rating = if (rating == it) {
                        0
                    } else {
                        it
                    }
                    productsService.rateProduct(productId, rating)
                    canChangeRating = true
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(0.8f)
        ) {
            repeat(rating) {
                IconButton(
                    onClick = { changeRating(it + 1) },
                    icon = Icons.Filled.Star,
                    description = "star",
                    selected = true,
                    enabled = canChangeRating
                )
            }
            repeat(5 - rating) {
                IconButton(
                    onClick = { changeRating(rating + it + 1) },
                    icon = Icons.Filled.StarBorder,
                    description = "star",
                    enabled = canChangeRating
                )
            }
        }

    }
}
