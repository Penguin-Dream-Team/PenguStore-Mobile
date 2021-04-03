package store.pengu.mobile.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.views.cart.CartScreen
import store.pengu.mobile.views.dashboard.DashboardScreen
import store.pengu.mobile.views.dashboard.partials.SetupScreen
import store.pengu.mobile.views.lists.ListsScreen
import store.pengu.mobile.views.lists.partials.NewPantryList
import store.pengu.mobile.views.lists.partials.PantryList
import store.pengu.mobile.views.lists.partials.NewShoppingList
import store.pengu.mobile.views.lists.partials.ShoppingList
import store.pengu.mobile.views.profile.ProfileScreen
import store.pengu.mobile.views.search.SearchScreen
import store.pengu.mobile.views.shared.BottomBar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var storeState: StoreState

    @Inject
    lateinit var api: PenguStoreApi

    private var navController: NavHostController? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        lifecycleScope.launchWhenCreated {
            try {
                storeState.products = api.products().data
                storeState.pantryLists = api.pantries().data
                storeState.shoppingLists = emptyList()
                storeState.lists = arrayOf(storeState.pantryLists, storeState.shoppingLists)

            } catch(e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            val navController = rememberNavController()
            this.navController = navController

            PenguShopTheme {
                Scaffold(bottomBar = { BottomBar(navController) }) {
                    NavHost(navController = navController, startDestination = "dashboard") {
                        composable("dashboard") {
                            DashboardScreen(navController, storeState)
                        }

                        composable("setup") {
                            SetupScreen(navController, storeState)
                        }

                        composable("lists") {
                            ListsScreen(navController, storeState)
                        }

                        composable("new_pantry_list") {
                            NewPantryList(navController, this@MainActivity)
                        }

                        composable("pantry_list") {
                            PantryList(navController, storeState)
                        }

                        composable("new_shopping_list") {
                            NewShoppingList(navController, this@MainActivity)
                        }

                        composable("shopping_list") {
                            ShoppingList(navController, storeState)
                        }

                        composable("search") {
                            SearchScreen(navController, storeState)
                        }

                        composable("cart") {
                            CartScreen(navController)
                        }

                        composable("profile") {
                            ProfileScreen(navController)
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == RESULT_OK && requestCode == 111) {
            storeState.listLocation = LatLng(
                data.extras!!.getDouble("LATITUDE"),
                data.extras!!.getDouble("LONGITUDE")
            )
        }
    }
}