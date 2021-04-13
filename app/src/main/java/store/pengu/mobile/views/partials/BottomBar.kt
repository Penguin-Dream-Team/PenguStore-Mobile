package store.pengu.mobile.views.partials

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate

@Composable
fun BottomBar(navController: NavHostController) {
    BottomAppBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        )
        {
            IconButton(
                { navigate(navController, "dashboard") },
                Icons.Filled.Home,
                "Home",
                currentRoute == "dashboard"
            )
            IconButton(
                { navigate(navController, "search") },
                Icons.Filled.Search,
                "Search",
                currentRoute == "search"
            )
            IconButton(
                { navigate(navController, "cart") },
                Icons.Filled.ShoppingCart,
                "ShoppingCart",
                currentRoute == "cart"
            )
            IconButton(
                { navigate(navController, "profile") },
                Icons.Filled.AccountCircle,
                "Profile",
                currentRoute == "profile"
            )
        }
    }
}

@SuppressLint("RestrictedApi")
fun navigate(navController: NavHostController, location: String) {
    //navController.backStack.clear()
    navController.navigate(location)
}
