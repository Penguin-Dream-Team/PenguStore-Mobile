package store.pengu.mobile.views.lists.partials

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.states.StoreState

@Composable
fun ShoppingList(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val selectedPantry = storeState.selectedList as ShoppingList

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            selectedPantry.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Shopping List View",
            textAlign = TextAlign.Center
        )
    }
}