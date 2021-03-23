package store.pengu.mobile.views.cart

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate

@Composable
fun CartScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colors.onBackground)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxHeight()
                    //.scrollable(state = orientation = Orientation.Horizontal)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    //val image: Painter = painterResource(id = R.drawable.ic_logo)
                    //Image(painter = image, contentDescription = "product_picture")
                    Text(text = "This should be an image", color = MaterialTheme.colors.primary)
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Text(text = "product_name", color = MaterialTheme.colors.primary)
                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            Text(text = "product_price", color = MaterialTheme.colors.primary)
                            Text(text = "product_quantity", color = MaterialTheme.colors.primary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colors.onSurface)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(text = "Subtotal", color = MaterialTheme.colors.primary)
                    Text(text = "420.00€", color = MaterialTheme.colors.primary)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(text = "Shipping Cost", color = MaterialTheme.colors.primary)
                    Text(text = "69.00€", color = MaterialTheme.colors.primary)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(text = "Taxes", color = MaterialTheme.colors.primary)
                    Text(text = "69.00€", color = MaterialTheme.colors.primary)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(text = "Total", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
                    Text(text = "420.69€", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
                }
            }
        }
        
        Button(
            onClick = {
                navController.navigate("new_pantry")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Checkout",
                textAlign = TextAlign.Center
            )
        }
    }
}