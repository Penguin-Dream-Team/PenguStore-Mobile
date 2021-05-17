package store.pengu.mobile.views.lists.partials

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.CoilImage
import store.pengu.mobile.R
import store.pengu.mobile.utils.Border
import store.pengu.mobile.utils.border
import store.pengu.mobile.utils.toColor
import store.pengu.mobile.views.partials.AnimatedShimmerLoading

@ExperimentalAnimationApi
@Composable
fun ProductItem(
    title: String,
    haveAmount: Int,
    needAmount: Int,
    color: String,
    image: String? = null,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(5),
        elevation = 1.dp,
        modifier = Modifier
            .clickable(onClick = onClick, onClickLabel = stringResource(R.string.open) + title)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .border(
                    bottom = Border(
                        3.dp,
                        color
                            .toColor()
                            .copy(alpha = 0.3f)
                    )
                )
                .padding(bottom = 3.dp)
        ) {
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shadow(elevation = 1.dp, shape = CircleShape)
            ) {
                CoilImage(
                    data = image ?: "",
                    contentDescription = title,
                    fadeIn = true,
                    contentScale = ContentScale.Crop,
                    error = {
                        Image(
                            painter = painterResource(id = R.drawable.default_image), title,
                            contentScale = ContentScale.Crop
                        )
                    },
                    loading = {
                        AnimatedShimmerLoading()
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1.0f, true),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        maxLines = 1,
                        fontSize = 20.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "more info",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .padding(bottom = 5.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 10.dp),
                    ) {
                        Text(
                            text = "$haveAmount",
                            modifier = Modifier
                                .padding(end = 5.dp),
                            fontSize = 16.sp,
                        )
                        Icon(
                            imageVector = Icons.Filled.Inventory2,
                            modifier = Modifier
                                .alpha(0.7f)
                                .size(14.dp),
                            contentDescription = stringResource(R.string.have)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 10.dp),
                    ) {
                        Text(
                            text = "$needAmount",
                            modifier = Modifier
                                .padding(end = 5.dp),
                            fontSize = 16.sp,
                        )
                        Icon(
                            imageVector = Icons.Filled.ShoppingBasket,
                            modifier = Modifier
                                .alpha(0.7f)
                                .size(14.dp),
                            contentDescription = stringResource(R.string.need),
                        )
                    }
                }
            }
        }
    }
}
