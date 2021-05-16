package store.pengu.mobile.views.products.partials

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.pengu.mobile.data.productlists.ProductPantryListEntry
import store.pengu.mobile.utils.Border
import store.pengu.mobile.utils.border
import store.pengu.mobile.utils.geo.GeoUtils
import store.pengu.mobile.utils.toColor

@Composable
fun PantryListCard(
    productListEntry: ProductPantryListEntry,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    with(productListEntry) {
        Surface(
            shape = RoundedCornerShape(5),
            elevation = 1.dp,
            modifier = Modifier
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    onClickLabel = "Edit list $listName"
                )
        ) {
            Column(
                modifier = Modifier
                    .border(start = Border(5.dp, color.toColor()))
                    .padding(start = 5.dp)
                    .fillMaxWidth(),
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
                        text = listName,
                        maxLines = 1,
                        fontSize = MaterialTheme.typography.h5.fontSize
                    )
                    if (isShared) {
                        Icon(imageVector = Icons.Filled.People, contentDescription = "shared")
                    } else {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = "private")
                    }
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .padding(bottom = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(end = 10.dp),
                        ) {
                            Text(
                                text = "$amountAvailable",
                                modifier = Modifier
                                    .padding(end = 5.dp),
                                fontSize = 16.sp,
                            )
                            Icon(
                                imageVector = Icons.Filled.Inventory2,
                                modifier = Modifier
                                    .alpha(0.7f)
                                    .size(14.dp),
                                contentDescription = "have"
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(start = 10.dp),
                        ) {
                            Text(
                                text = "$amountNeeded",
                                modifier = Modifier
                                    .padding(end = 5.dp),
                                fontSize = 16.sp,
                            )
                            Icon(
                                imageVector = Icons.Filled.ShoppingBasket,
                                modifier = Modifier
                                    .alpha(0.7f)
                                    .size(14.dp),
                                contentDescription = "need",
                            )
                        }
                    }
                    Text(
                        text = GeoUtils.getLocationName(context, latitude, longitude),
                        modifier = Modifier
                            .alpha(0.8f)
                            .padding(start = 15.dp),
                        fontSize = MaterialTheme.typography.caption.fontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
