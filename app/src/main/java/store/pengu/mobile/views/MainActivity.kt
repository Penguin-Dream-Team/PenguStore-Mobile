package store.pengu.mobile.views

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.api.Response
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

    @Inject
    lateinit var api: PenguStoreApi

    private var navController: NavHostController? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        lifecycleScope.launchWhenCreated {
            try {
                val res = api.dashboard()
                Toast.makeText(applicationContext, res.data, Toast.LENGTH_LONG).show()
            } catch(e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }

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
                            NewPantry(navController, applicationContext)
                        }

                        composable("shop") {
                            ShopScreen(navController)
                        }
                    }
                }
            }
        }
    }
}