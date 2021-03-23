package store.pengu.mobile.views

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.views.dashboard.DashboardScreen
import store.pengu.mobile.views.dashboard.partials.SetupScreen
import store.pengu.mobile.views.pantry.PantryScreen
import store.pengu.mobile.views.pantry.partials.NewPantry
import store.pengu.mobile.views.shared.TopBar
import store.pengu.mobile.views.shop.ShopScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var storeState: StoreState

    private var navController: NavHostController? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            val navController = rememberNavController()
            this.navController = navController

            PenguShopTheme {
                Scaffold(topBar = { TopBar(navController) }) {
                    NavHost(navController = navController, startDestination = "dashboard") {
                        composable("dashboard") {
                            DashboardScreen(navController, storeState)
                        }

                        composable("setup") {
                            SetupScreen(navController, storeState)
                        }

                        composable("pantry") {
                            PantryScreen(navController)
                        }

                        composable("new_pantry") {
                            NewPantry(navController, storeState)
                        }

                        composable("shop") {
                            ShopScreen(navController)
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }
}