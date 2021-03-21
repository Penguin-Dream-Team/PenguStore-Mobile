package store.pengu.mobile.views.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.dashboard.partials.SetupScreen
import store.pengu.mobile.views.dashboard.partials.UserInfo
import store.pengu.mobile.views.pantry.PantryScreen
import store.pengu.mobile.views.shop.ShopScreen

@Composable
fun DashboardScreen(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }

    Column {
        if (storeState.userType == "") {
           SetupScreen(navController, storeState)
        }
        else {
            UserInfo(storeState)

            PantryScreen(navController)

            Spacer(modifier = Modifier.preferredHeight(32.dp))

            ShopScreen(navController)
        }
    }
}
