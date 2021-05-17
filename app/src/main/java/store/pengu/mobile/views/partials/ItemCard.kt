package store.pengu.mobile.views.partials

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.google.accompanist.coil.CoilImage
import com.google.accompanist.coil.LocalImageLoader
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import store.pengu.mobile.R
import store.pengu.mobile.utils.Math
import store.pengu.mobile.utils.lighten

@ExperimentalAnimationApi
@Composable
fun ItemCard(
    imageLoader: ImageLoader,
    name: String,
    modifier: Modifier = Modifier,
    tagLine: String? = null,
    image: String? = null
) {
    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        val imagePainter = rememberCoilPainter(
            request = image,
            fadeIn = true,
        )
        val height = if (tagLine.isNullOrBlank()) 150.dp else 190.dp
        BoxWithConstraints(
            modifier = Modifier
                .then(modifier)
                .width(150.dp)
                .height(height)
                .border(
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f),
                    width = 2.dp,
                    shape = RoundedCornerShape(8)
                )
                .clip(shape = RoundedCornerShape(8))
        ) {
            val maxHeight = maxHeight
            val imageSize = Math.min(maxWidth - 40.dp, 100.dp)
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xff64b3f4),
                                Color(0xffc2e59c)
                            )
                        )
                    )
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.background.lighten(0.05f))
                        .fillMaxWidth()
                        .height(maxHeight * 4 / 7)
                        .padding(
                            top = imageSize / 2,
                            start = 10.dp,
                            end = 10.dp
                        )
                ) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground,
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        maxLines = 1
                    )
                    if (!tagLine.isNullOrBlank()) {
                        Text(
                            text = tagLine,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                            maxLines = 1
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(
                        y = (maxHeight * 3 / 7) - imageSize / 2,
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(imageSize)
                        .shadow(elevation = 1.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .padding(all = 5.dp)
                ) {
                    Image(
                        painter = imagePainter,
                        contentDescription = "product image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(imageSize)
                            .clip(CircleShape)
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
                                .clip(CircleShape)
                                .align(Alignment.Center)
                        )
                        else -> Unit
                    }
                }
            }
        }
    }
}