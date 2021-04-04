package store.pengu.mobile.views.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.LoginService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.dashboard.partials.SetupScreen
import store.pengu.mobile.views.lists.ListsScreen

@Composable
fun DashboardScreen(
    navController: NavController,
    loginService: LoginService,
    listsService: ListsService,
    store: StoreState
) {
    val storeState by remember { mutableStateOf(store) }

    Column (
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
    ) {
        if (storeState.userId == -1L)
            SetupScreen(navController, loginService)
        else
            ListsScreen(navController, listsService, storeState)
    }
}
