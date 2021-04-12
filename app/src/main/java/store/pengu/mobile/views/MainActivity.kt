package store.pengu.mobile.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.LoginService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.views.cart.CartConfirmationScreen
import store.pengu.mobile.views.cart.CartScreen
import store.pengu.mobile.views.dashboard.DashboardScreen
import store.pengu.mobile.views.dashboard.partials.SetupScreen
import store.pengu.mobile.views.lists.ListsScreen
import store.pengu.mobile.views.lists.partials.NewList
import store.pengu.mobile.views.lists.partials.PantryList
import store.pengu.mobile.views.lists.partials.ShoppingList
import store.pengu.mobile.views.partials.BottomBar
import store.pengu.mobile.views.profile.ProfileScreen
import store.pengu.mobile.views.search.SearchScreen
import store.pengu.mobile.views.splash.SplashScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var loginService: LoginService

    @Inject
    lateinit var listsService: ListsService

    @Inject
    lateinit var productsService: ProductsService

    @Inject
    lateinit var accountService: AccountService

    @Inject
    lateinit var storeState: StoreState

    @Inject
    lateinit var api: PenguStoreApi

    private var navController: NavHostController? = null

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //productsService.getProducts()

        setContent {
            val navController = rememberNavController()
            this.navController = navController
            var showBottomBar by remember { mutableStateOf(false) }

            val startDestination = runBlocking {
                if (accountService.hasLoggedInBefore()) {
                    "splash"
                } else {
                    "dashboard"
                }
            }

            PenguShopTheme {
                Scaffold(bottomBar = {
                    if (showBottomBar) {
                        BottomBar(navController)
                    }
                }) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("splash") {
                            showBottomBar = false
                            SplashScreen(navController, accountService)
                        }

                        composable("dashboard") {
                            showBottomBar = true
                            DashboardScreen(navController, loginService, listsService, storeState)
                        }

                        composable("setup") {
                            showBottomBar = true
                            SetupScreen(navController, loginService)
                        }

                        composable("lists") {
                            showBottomBar = true
                            ListsScreen(navController, listsService, storeState)
                        }

                        composable("new_list") {
                            showBottomBar = true
                            NewList(navController, listsService, this@MainActivity, storeState)
                        }

                        composable("pantry_list") {
                            showBottomBar = true
                            PantryList(navController, productsService, storeState)
                        }

                        composable("shopping_list") {
                            showBottomBar = true
                            ShoppingList(navController, productsService, storeState)
                        }

                        composable("search") {
                            showBottomBar = true
                            SearchScreen(navController, productsService, storeState)
                        }

                        composable("cart") {
                            showBottomBar = true
                            CartScreen(navController, storeState)
                        }

                        composable("cart_confirmation") {
                            showBottomBar = true
                            CartConfirmationScreen(navController, storeState)
                        }

                        composable("profile") {
                            showBottomBar = true
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