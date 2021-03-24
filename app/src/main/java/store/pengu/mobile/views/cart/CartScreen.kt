package store.pengu.mobile.views.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.views.shared.CartProductRow

@Composable
fun CartScreen(navController: NavController) {
    Column(verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colors.onBackground)
        ) {
            val scrollState = rememberScrollState()
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(state = scrollState)
            ) {
                CartProductRow ("Image", "Product_Name", "Product_Price", "Product_Quantity")
                CartProductRow ("Image", "Product_Name", "Product_Price", "Product_Quantity")
                CartProductRow ("Image", "Product_Name", "Product_Price", "Product_Quantity")
                CartProductRow ("Image", "Product_Name", "Product_Price", "Product_Quantity")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("new_pantry")
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Checkout",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
}