package store.pengu.mobile.views.lists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.lists.partials.ListItem
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.data.ShoppingList2
import store.pengu.mobile.theme.shop
import store.pengu.mobile.utils.GeoUtils
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.toColor
import store.pengu.mobile.views.partials.pulltorefresh.PullToRefresh

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ListsScreen(
    navController: NavController,
    listsService: ListsService,
    store: StoreState,
    snackbarController: SnackbarController,
) {
    var selectedList by store.selectedListType
    val pantryLists = listsService.pantryLists
    val shoppingLists = listsService.shoppingLists

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .padding(top = 10.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedList,
            contentColor = MaterialTheme.colors.primary,
        ) {
            Tab(
                modifier = Modifier.padding(top = 15.dp, bottom = 13.dp),
                selected = selectedList == 0,
                content = { Text("Pantries") },
                onClick = { selectedList = 0 },
                unselectedContentColor = MaterialTheme.colors.onSurface
            )
            Tab(
                modifier = Modifier.padding(top = 15.dp, bottom = 13.dp),
                selected = selectedList == 1,
                content = { Text("Shopping") },
                onClick = { selectedList = 1 },
                unselectedContentColor = MaterialTheme.colors.onSurface
            )
        }
        ListsTab(visible = selectedList == 0, lists = pantryLists, onRefresh = {
            listsService.getPantryLists()
        }) { item, enabled ->
            ListItem(
                title = item.name,
                productAmount = item.productCount,
                location = GeoUtils.getLocationName(context, item.latitude, item.longitude),
                color = item.color.toColor(),
                isShared = item.shared,
                enabled = enabled
            ) {
                store.selectedList = item
                navController.navigate("pantry_list")
            }
        }

        ListsTab(visible = selectedList == 1, lists = shoppingLists, onRefresh = {
            listsService.getShoppingLists()
        }) { item, enabled ->
            ListItem(
                title = item.name,
                //productAmount = it.productCount,
                productAmount = 0,
                location = GeoUtils.getLocationName(context, item.latitude, item.longitude),
                color = item.color.toColor(),
                isShared = item.shared,
                enabled = enabled
            ) {
                store.selectedList = item
                navController.navigate("shopping_list")
            }
        }
    }
}
