package store.pengu.mobile.views.partials

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import store.pengu.mobile.R
import java.util.*

private var lists = mutableStateOf("")
private var newItem = mutableStateOf("")
private var cart = mutableStateOf("")
private var profile = mutableStateOf("")

private val bottomBarItems: List<BottomBarItem>
    get() = listOf(
        BottomBarItem(
            lists.value, Icons.Filled.List,
            listOf(
                "pantry_list/{pantryId}",
                "shopping_list/{shopId}",
            ),
            location = "lists"
        ),
        BottomBarItem(
            newItem.value,
            Icons.Filled.Add,
            listOf(
                "search?shopId={shopId}&pantryId={pantryId}",
                "add_product_to_list/{productId}?listType={listType}&listId={listId}",
                "new_item?shopId={shopId}&pantryId={pantryId}"
            ),
            location = "new_item"
        ),
        BottomBarItem(cart.value, Icons.Filled.ShoppingCart, location = "cart"),
        BottomBarItem(profile.value, Icons.Filled.AccountCircle, location = "profile"),
    )

private val bottomBarButtonItems = listOf(
    "lists",
    "pantry_list/{pantryId}",
    "shopping_list/{shopId}",
    "add_product_to_list/{productId}?listType={listType}&listId={listId}",
    "product/{productId}"
)

data class BottomBarItem(
    val name: String,
    val icon: ImageVector,
    val active: List<String> = listOf(),
    val location: String
) {
    val locations = listOf(location) + active
}

@ExperimentalAnimationApi
@Composable
fun BottomBar(navController: NavHostController, buttonShape: RoundedCornerShape) {
    lists.value = stringResource(R.string.lists)
    newItem.value = stringResource(R.string.new_item)
    cart.value = stringResource(R.string.cart)
    profile.value = stringResource(R.string.profile)

    BottomAppBar(cutoutShape = buttonShape) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        )
        {
            bottomBarItems.forEachIndexed { index, item ->
                val selected = item.locations.contains(currentRoute)
                BottomNavigationItem(
                    selected = selected,
                    onClick = { navigate(navController, item.location) },
                    icon = { Icon(item.icon, item.name) },
                    label = { Text(item.name, maxLines = 1, softWrap = false) },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                )

                // Middle spacer
                if (index + 1 == bottomBarItems.size / 2) {
                    AnimatedVisibility(
                        visible = bottomBarButtonItems.contains(currentRoute),
                        enter = expandHorizontally(),
                        exit = shrinkHorizontally(),
                        modifier = Modifier.weight(1f, true)
                    ) {
                        Spacer(modifier = Modifier.weight(1f, true))
                    }
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
fun navigate(navController: NavHostController, location: String) {
    navController.navigate(location)
}
