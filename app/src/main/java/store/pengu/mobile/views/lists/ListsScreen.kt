package store.pengu.mobile.views.lists

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.R
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.lists.partials.ListItem
import store.pengu.mobile.utils.GeoUtils
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.toColor

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
                content = { Text(stringResource(R.string.pantries)) },
                onClick = { selectedList = 0 },
                unselectedContentColor = MaterialTheme.colors.onSurface
            )
            Tab(
                modifier = Modifier.padding(top = 15.dp, bottom = 13.dp),
                selected = selectedList == 1,
                content = { Text(stringResource(R.string.shopping)) },
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
