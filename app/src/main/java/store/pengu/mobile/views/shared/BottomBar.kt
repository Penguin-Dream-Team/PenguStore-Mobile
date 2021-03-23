package store.pengu.mobile.views.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate

@Composable
fun BottomBar(navController: NavHostController) {
    BottomAppBar() {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        )
        {
            IconButton({ navigate(navController,"dashboard") }, Icons.Filled.Home, "Home", true)
            IconButton({ navigate(navController,"search") }, Icons.Filled.Search, "Search", true)
            IconButton({ navigate(navController,"cart") }, Icons.Filled.ShoppingCart, "ShoppingCart", true)
            IconButton({ navigate(navController,"profile") }, Icons.Filled.AccountCircle, "Profile", true)
        }
    }
}

@SuppressLint("RestrictedApi")
fun navigate(navController: NavHostController, location: String) {
    navController.backStack.clear()
    navController.navigate(location)
}
