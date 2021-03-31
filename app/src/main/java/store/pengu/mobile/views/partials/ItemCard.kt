package store.pengu.mobile.views.partials

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.google.accompanist.coil.CoilImage
import store.pengu.mobile.R

@ExperimentalAnimationApi
@Composable
fun ItemCard(
    name: String,
    modifier: Modifier = Modifier,
    tagLine: String? = null,
    image: String? = null
) {
    BoxWithConstraints(
        modifier = Modifier
            .then(modifier)
            .width(150.dp)
            .height(190.dp)
            .border(
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f),
                width = 2.dp,
                shape = RoundedCornerShape(8)
            )
            .clip(shape = RoundedCornerShape(8))
    ) {
        val maxHeight = maxHeight
        val imageSize = 100.dp
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
                    .background(color = MaterialTheme.colors.background)
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
            Surface(
                modifier = Modifier
                    .width(imageSize)
                    .height(imageSize)
                    .shadow(elevation = 1.dp, shape = CircleShape)
                    .clip(CircleShape)
            ) {
                CoilImage(
                    data = image ?: "",
                    contentDescription = name,
                    fadeIn = true,
                    contentScale = ContentScale.Crop,
                    error = {
                        Image(
                            painter = painterResource(id = R.drawable.default_image), name,
                            contentScale = ContentScale.Crop
                        )
                    },
                    loading = {
                        AnimatedShimmerLoading()
                    }
                )
            }
        }
    }
}


@ExperimentalAnimationApi
@Preview
@Composable
fun ItemCardPreview() {
    ItemCard(
        name = "Pug",
        tagLine = "This is a dog",
        image = "https://files.perpheads.com/TxGQwRWwBhg870HC.png"
    )
}