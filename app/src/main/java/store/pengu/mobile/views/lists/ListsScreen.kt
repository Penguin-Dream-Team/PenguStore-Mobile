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
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.lists.partials.ListItem
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList2
import store.pengu.mobile.utils.SnackbarController
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
    var selectedList by remember { store.selectedListType }
    val pantryLists: MutableList<PantryList> = remember {
        mutableListOf(
            PantryList(1, "1", "Lista 1", 100f, 200f, 2, true),
            PantryList(2, "2", "Lista 2", 20f, 5f, 3),
            PantryList(3, "3", "Lista 3", 10f, 800f, 20),
            PantryList(4, "4", "Lista 4", 400f, 600f, 15, true),
            PantryList(5, "5", "Lista 5", 600f, 220f, 50),
        )
    }
    val shoppingLists: MutableList<ShoppingList2> = remember {
        mutableListOf(
            ShoppingList2(1, 2, "Shop 1"),
            ShoppingList2(2, 2, "Shop 2"),
            ShoppingList2(3, 2, "Shop 3"),
            ShoppingList2(4, 2, "Shop 4"),
            ShoppingList2(5, 2, "Shop 5"),
            ShoppingList2(6, 2, "Shop 6"),
            ShoppingList2(1, 2, "Shop 1"),
            ShoppingList2(2, 2, "Shop 2"),
            ShoppingList2(3, 2, "Shop 3"),
            ShoppingList2(4, 2, "Shop 4")
        )
    }
    val coroutineScope = rememberCoroutineScope()

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
        AnimatedVisibility(visible = selectedList == 0) {
            var isRefreshing: Boolean by remember { mutableStateOf(false) }

            PullToRefresh(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    // update items and set isRefreshing = false
                    coroutineScope.launch {
                        delay(1000L)
                        pantryLists.reverse()
                        isRefreshing = false
                    }
                }
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 15.dp)
                ) {
                    items(pantryLists) { item ->
                        ListItem(
                            title = item.name,
                            productAmount = item.productCount,
                            location = "${item.latitude}, ${item.longitude}",
                            color = Color.Red,
                            isShared = item.isShared,
                            enabled = !isRefreshing
                        ) {
                            snackbarController.showDismissibleSnackbar("Clicked ${item.name}")
                        }
                    }
                }
            }
        }
        AnimatedVisibility(visible = selectedList == 1) {
            var isRefreshing: Boolean by remember { mutableStateOf(false) }

            PullToRefresh(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    // update items and set isRefreshing = false
                    coroutineScope.launch {
                        delay(1000L)
                        shoppingLists.reverse()
                        isRefreshing = false
                    }
                }
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 15.dp)
                ) {
                    items(shoppingLists) { item ->
                        ListItem(
                            title = item.name,
                            productAmount = 0,
                            location = "Somewhere",
                            color = Color.Green,
                            isShared = false,
                            enabled = !isRefreshing
                        ) {
                            snackbarController.showDismissibleSnackbar("Clicked ${item.name}")
                        }
                    }
                }
            }
        }
    }
}