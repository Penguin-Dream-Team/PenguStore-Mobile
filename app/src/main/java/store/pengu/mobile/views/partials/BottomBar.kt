package store.pengu.mobile.views.partials

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private val bottomBarItems = listOf(
    BottomBarItem("Lists", Icons.Filled.List),
    BottomBarItem("Search", Icons.Filled.Search),
    BottomBarItem("Cart", Icons.Filled.ShoppingCart),
    BottomBarItem("Profile", Icons.Filled.AccountCircle),
)

private val bottomBarButtonItems = listOf(
    "lists"
)

data class BottomBarItem(
    val name: String,
    val icon: ImageVector
)

@ExperimentalAnimationApi
@Composable
fun BottomBar(navController: NavHostController, buttonShape: RoundedCornerShape) {
    BottomAppBar(cutoutShape = buttonShape) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        )
        {
            bottomBarItems.forEachIndexed { index, item ->
                val selected = currentRoute == item.name.toLowerCase(Locale.ENGLISH)
                BottomNavigationItem(
                    selected = selected,
                    onClick = { navigate(navController, item.name.toLowerCase(Locale.ENGLISH)) },
                    icon = { Icon(item.icon, item.name) },
                    label = { Text(item.name) },
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
