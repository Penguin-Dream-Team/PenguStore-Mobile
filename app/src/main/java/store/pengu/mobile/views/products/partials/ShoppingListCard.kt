package store.pengu.mobile.views.products.partials

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import store.pengu.mobile.data.productlists.ProductShoppingListEntry
import store.pengu.mobile.utils.Border
import store.pengu.mobile.utils.border
import store.pengu.mobile.utils.geo.GeoUtils
import store.pengu.mobile.utils.toColor

@Composable
fun ShoppingListCard(
    listEntry: ProductShoppingListEntry,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    with(listEntry) {
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
                            .padding(horizontal = 5.dp)
                            .padding(bottom = 5.dp)
                    ) {
                        Text(
                            text = "$price",
                            modifier = Modifier
                                .padding(end = 3.dp),
                            fontSize = 16.sp,
                        )
                        Icon(
                            imageVector = Icons.Filled.EuroSymbol,
                            modifier = Modifier
                                .alpha(0.7f)
                                .size(16.dp)
                                .padding(top = 1.dp),
                            contentDescription = "price"
                        )
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
