package store.pengu.mobile.views.search.partials

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import store.pengu.mobile.data.Product
import store.pengu.mobile.states.StoreState
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import store.pengu.mobile.R

import kotlin.math.max
import kotlin.math.min
import android.content.Intent

import androidx.core.content.ContextCompat.startActivity
import store.pengu.mobile.views.MainActivity


private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun ProductScreen(
    navController: NavHostController,
    mainActivity: MainActivity,
    store: StoreState
) {
    val storeState by remember { mutableStateOf(store) }
    val selectedProduct = storeState.selectedProduct

    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        Body(scroll)
        Title(selectedProduct!!, scroll.value)
        Image(""/*selectedProduct.imageUrl*/, scroll.value)
        Back(navController)
        Share(mainActivity)
        CartBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.secondaryVariant)//Brush.horizontalGradient(PenguShopTheme {}.colors.interactivePrimary))
    )
}

@Composable
private fun Body(
    scroll: ScrollState
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier
                .verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))

            Surface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))

                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Details",
                        style = MaterialTheme.typography.overline,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Details Place Holder",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(40.dp))

                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.overline,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Stuff",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Stuff",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Stuff",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Stuff",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Stuff",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Stuff",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Stuff",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))

                    Divider(
                        modifier = Modifier,
                        thickness = 1.dp,
                        startIndent = 0.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun Title(
    product: Product,
    scroll: Int
) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }
    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .graphicsLayer { translationY = offset }
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
            modifier = HzPadding
        )

        Spacer(Modifier.height(8.dp))

        Divider(
            modifier = Modifier,
            thickness = 1.dp,
            startIndent = 0.dp
        )

        /*Text(
            text = "",
            style = MaterialTheme.typography.subtitle2,
            fontSize = 20.sp,
            color = MaterialTheme.colors.secondary,
            modifier = HzPadding
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.secondary,
            modifier = HzPadding
        ))*/
    }
}

@SuppressLint("ResourceType")
@Composable
private fun Image(
    imageUrl: String,
    scroll: Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFraction = (scroll / collapseRange).coerceIn(0f, 1f)

    CollapsingImageLayout(
        collapseFraction = collapseFraction,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {
        Surface(
            color = Color.LightGray,
            elevation = 0.dp,
            shape = CircleShape,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = if(imageUrl == "") painterResource(id = R.drawable.default_image)
                            else rememberCoilPainter(imageUrl),
                contentDescription = "image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun Back(navController: NavHostController) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = MaterialTheme.colors.primaryVariant,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = "Back"
        )
    }
}

@Composable
private fun Share(mainActivity: MainActivity) {
    IconButton(
        onClick = {
            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.type = "text/plain"
            val shareBody = "Share your Product"
            val shareSub = "Share it on social Media"
            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            mainActivity.startActivity(Intent.createChooser(myIntent, "Share"))
        },
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 116.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = MaterialTheme.colors.primaryVariant,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Share,
            contentDescription = "Share"
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFraction: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.place(imageX, imageY)
        }
    }
}

@Composable
private fun CartBottomBar(modifier: Modifier = Modifier) {
    val (count, updateCount) = remember { mutableStateOf(1) }

    Surface(modifier) {
        Column {
            Divider(
                modifier = Modifier,
                thickness = 1.dp,
                startIndent = 0.dp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .navigationBarsPadding(left = false, right = false)
                    .then(HzPadding)
                    .heightIn(min = BottomBarHeight)
            ) {
                // TODO add to cart or list

                Spacer(Modifier.width(16.dp))

                Button(
                    onClick = { /* todo */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Add to cart",
                        maxLines = 1
                    )
                }
            }
        }
    }
}