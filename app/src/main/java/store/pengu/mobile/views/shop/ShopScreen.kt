package store.pengu.mobile.views.shop

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate

@Composable
fun ShopScreen(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Button(
            onClick = {
                navController.navigate("shop")
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Shop",
                textAlign = TextAlign.Center
            )
        }
    }
}