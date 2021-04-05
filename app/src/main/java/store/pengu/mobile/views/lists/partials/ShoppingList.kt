package store.pengu.mobile.views.lists.partials

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState

@Composable
fun ShoppingList(navController: NavController, productsService: ProductsService, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val selectedShoppingList = storeState.selectedList as ShoppingList
    val products by remember { mutableStateOf(store.shoppingListProducts) }

    productsService.getShoppingListProducts(selectedShoppingList.userId)

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            selectedShoppingList.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Shopping List View",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(products) { product ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${product.name}: ${product.amountAvailable} out of ${product.amountNeeded}",
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(
                        onClick = {
                            // Add to cart
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddShoppingCart,
                            tint = Color(52, 247, 133),
                            contentDescription = "Add to Cart"
                        )
                    }
                }
            }
        }
    }
}