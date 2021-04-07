package store.pengu.mobile.views.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CartProductRow(image: String, productName: String, productPrice: String, productQuantity: String) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colors.secondary)
        ) {
            //val image: Painter = painterResource(id = R.drawable.ic_logo)
            //Image(painter = image, contentDescription = "product_picture")
            Text(text = image, color = MaterialTheme.colors.primary)
        }

        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(6.dp)
                .clip(RoundedCornerShape(10.dp))
                //.border(color = MaterialTheme.colors.primary)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = productName, color = MaterialTheme.colors.primary)
                }

                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text(text = productPrice, color = MaterialTheme.colors.primary)

                    Text(text = productQuantity, color = MaterialTheme.colors.primary)
                }
            }
        }
    }
}