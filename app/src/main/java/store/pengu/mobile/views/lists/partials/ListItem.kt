package store.pengu.mobile.views.lists.partials

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.utils.Border
import store.pengu.mobile.utils.border

@Composable
fun ListItem(
    title: String,
    productAmount: Int,
    location: String,
    color: Color,
    isShared: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(5),
        elevation = 1.dp,
        modifier = Modifier
            .clickable(onClick = onClick, onClickLabel = "Open $title", enabled = enabled)
    ) {
        Column(
            modifier = Modifier
                .border(start = Border(5.dp, color))
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
                    text = title,
                    maxLines = 1,
                    fontSize = MaterialTheme.typography.h5.fontSize
                )
                if (isShared) {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = "private")
                } else {
                    Icon(imageVector = Icons.Filled.People, contentDescription = "shared")
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
                ) {
                    Text(
                        text = "$productAmount",
                        modifier = Modifier
                            .padding(end = 5.dp),
                        fontSize = MaterialTheme.typography.h6.fontSize,
                    )
                    Text(
                        text = "products",
                        modifier = Modifier
                            .alpha(0.8f),
                        fontSize = MaterialTheme.typography.caption.fontSize,
                        lineHeight = MaterialTheme.typography.caption.fontSize
                    )
                }
                Text(
                    text = location,
                    modifier = Modifier
                        .alpha(0.8f),
                    fontSize = MaterialTheme.typography.caption.fontSize
                )
            }
        }
    }
}

@Preview
@Composable
fun ListItemPreview() {
    PenguShopTheme {
        Surface() {
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Spacer(modifier = Modifier.height(10.dp))
                repeat(5) {
                    ListItem(
                        title = "Frigorífico de Lisboa",
                        color = Color.Red,
                        productAmount = 32,
                        location = "IST, Alameda, Lisboa",
                        isShared = true,
                        enabled = true
                    ) { }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
