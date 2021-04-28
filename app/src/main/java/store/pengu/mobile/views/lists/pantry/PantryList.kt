package store.pengu.mobile.views.lists.pantry

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.partials.IconButton

@ExperimentalAnimationApi
@Composable
fun PantryList(
    navController: NavController,
    productsService: ProductsService,
    store: StoreState
) {
    val pantryList = store.selectedList as PantryList? ?: return

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                pantryList.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1.0f, true))
            IconButton(
                onClick = { navController.navigate("share_pantry_list") },
                icon = Icons.Filled.Share,
                description = "Share pantry"
            )
        }

        ViewPantryList(navController, productsService, store, pantryList)
    }
}