package store.pengu.mobile.views.products.ProductInfo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import store.pengu.mobile.R
import store.pengu.mobile.views.partials.AnimatedShimmerLoading
import kotlin.math.ceil
import kotlin.math.roundToInt

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ProductGallery(
    images: List<String>
) {
    var showcaseImage by remember { mutableStateOf(null as Painter?) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = stringResource(R.string.gallery),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Divider(modifier = Modifier.padding(top = 3.dp, bottom = 5.dp))

        AnimatedVisibility(visible = images.isEmpty()) {
            Text(text = stringResource(R.string.no_images))
        }
        rememberScrollState()

        AnimatedVisibility(visible = images.isNotEmpty()) {
            val rowSize = 3
            val totalCount = images.size
            val rowCount = ceil(totalCount.toDouble() / rowSize).roundToInt()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                repeat(rowCount) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val offset = it * rowSize
                        ProductImage(images[offset], onClick = { showcaseImage = it })
                        if (offset + 1 < totalCount) {
                            ProductImage(images[offset + 1], onClick = { showcaseImage = it })
                            if (offset + 2 < totalCount) {
                                ProductImage(images[offset + 2], onClick = { showcaseImage = it })
                            }
                        }
                    }
                }
            }
        }

        if (showcaseImage != null) {
            Dialog(
                onDismissRequest = {
                    showcaseImage = null
                },
            ) {
                val imageSize = 250.dp
                Box(
                    modifier = Modifier
                        .size(imageSize)
                        .clip(RoundedCornerShape(5))
                        .background(MaterialTheme.colors.surface)
                ) {
                    Image(
                        painter = showcaseImage ?: painterResource(id = R.drawable.default_image),
                        contentDescription = "product image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(imageSize)
                    )
                    IconButton(
                        onClick = { showcaseImage = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close showcase",
                            modifier = Modifier
                                .background(MaterialTheme.colors.surface.copy(0.1f))
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun ProductImage(image: String, onClick: (Painter) -> Unit) {
    val imagePainter = rememberCoilPainter(
        request = image,
        fadeIn = true,
    )

    val imageSize = 100.dp
    Box(
        modifier = Modifier
            .size(imageSize)
            .clip(RoundedCornerShape(5))
            .padding(all = 5.dp)
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "product image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(5))
                .clickable(onClick = {
                    onClick(imagePainter)
                })
                .align(Alignment.Center)
        )
        when (imagePainter.loadState) {
            is ImageLoadState.Loading -> AnimatedShimmerLoading()
            is ImageLoadState.Empty,
            is ImageLoadState.Error -> Image(
                painterResource(R.drawable.default_image),
                contentDescription = "product image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(5))
                    .align(Alignment.Center)
            )
            else -> Unit
        }
    }
}
