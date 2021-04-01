package store.pengu.mobile.views

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.views.cart.CartScreen
import store.pengu.mobile.views.dashboard.DashboardScreen
import store.pengu.mobile.views.dashboard.partials.SetupScreen
import store.pengu.mobile.views.pantry.PantryScreen
import store.pengu.mobile.views.pantry.partials.NewPantry
import store.pengu.mobile.views.pantry.partials.Pantry
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
                storeState.pantries = api.pantries().data

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

                        composable("pantries") {
                            PantryScreen(navController, storeState)
                        }

                        composable("new_pantry") {
                            NewPantry(navController, LocalContext.current)
                        }

                        composable("pantry") {
                            Pantry(navController, storeState)
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
}