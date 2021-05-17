@file:Suppress("PackageName")

package store.pengu.mobile.views.products.partials

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.data.Product
import store.pengu.mobile.views.partials.AnimatedShimmerLoading

@ExperimentalAnimationApi
@Composable
fun Header(product: Product, ratingOverride: Double = product.productRating) {
    val imagePainter = rememberCoilPainter(
        request = product.image,
        fadeIn = true,
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
    ) {
        Box(
            modifier = Modifier
                .size(75.dp)
                .padding(end = 10.dp)
                .clip(RoundedCornerShape(5))
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "product image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(75.dp)
            )
            when (imagePainter.loadState) {
                is ImageLoadState.Loading -> AnimatedShimmerLoading()
                is ImageLoadState.Empty,
                is ImageLoadState.Error -> Image(
                    painterResource(R.drawable.default_image),
                    contentDescription = "product image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(75.dp)
                )
                else -> Unit
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.Label,
                    contentDescription = "product name",
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = product.name,
                    fontSize = 20.sp
                )
            }
            Row {
                Icon(
                    imageVector = Icons.Filled.QrCode,
                    contentDescription = "product barcode",
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = product.barcode ?: "- - -"
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f)
        ) {
            product.barcode?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${ratingOverride.run { if (this == 0.0) "-" else this }}",
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "product rating",
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }

            Button(onClick = {
                coroutineScope.launch {
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Checkout this awesome product!\nName: ${product.name}\n${
                                product.barcode?.run { "Barcode: $this\n" } ?: ""
                            }Rating: ${product.productRating} Stars"
                        )
                        putExtra(Intent.EXTRA_SUBJECT, "PenguStore Product")
                        //putExtra(Intent.EXTRA_STREAM, image)
                        //putExtra(Intent.EXTRA_STREAM, Uri.parse(image))
                        //type = "image/*"
                        type = "text/plain"

                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share"))
                }
            }) {
                Text(
                    text = "Share",
                    fontSize = 14.sp,
                )
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "product share",
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }
    }
}
