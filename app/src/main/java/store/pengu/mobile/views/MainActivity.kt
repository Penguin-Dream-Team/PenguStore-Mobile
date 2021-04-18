package store.pengu.mobile.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.android.gms.maps.model.LatLng
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast
import pt.inesc.termite.wifidirect.SimWifiP2pDevice
import pt.inesc.termite.wifidirect.SimWifiP2pManager
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel
import pt.inesc.termite.wifidirect.service.SimWifiP2pService
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.services.TermiteService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.cart.CartConfirmationScreen
import store.pengu.mobile.views.cart.CartScreen
import store.pengu.mobile.views.dashboard.DashboardScreen
import store.pengu.mobile.views.dashboard.partials.SetupScreen
import store.pengu.mobile.views.lists.ListsScreen
import store.pengu.mobile.views.lists.partials.NewList
import store.pengu.mobile.views.lists.partials.PantryList
import store.pengu.mobile.views.lists.partials.ShoppingList
import store.pengu.mobile.views.login.LoginScreen
import store.pengu.mobile.views.partials.BottomBar
import store.pengu.mobile.views.partials.PenguSnackbar
import store.pengu.mobile.views.partials.WifiP2pBroadcastReceiver
import store.pengu.mobile.views.profile.ProfileScreen
import store.pengu.mobile.views.search.SearchScreen
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PeerListListener {

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

    private val termiteService = TermiteService(this)
    private var navController: NavHostController? = null
    private var mReceiver: WifiP2pBroadcastReceiver? = null

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //productsService.getProducts()

        var loaded = false
        val startDestination: String

        // register broadcast receiver
        val filter = IntentFilter()
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION)
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION)
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION)
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION)
        mReceiver = WifiP2pBroadcastReceiver(this)
        registerReceiver(mReceiver, filter)

        runBlocking {
            accountService.loadData()
            startDestination = if (storeState.isLoggedIn()) {
                "dashboard"
            } else {
                "login"
            }
        }

        setContent {
            val navController = rememberNavController()
            this.navController = navController

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
            // put all routes that should not show bottom bar navigation
            val noBottomBarRoutes = listOf("login")
            val showBottomBar = !noBottomBarRoutes.contains(currentRoute)

            val scaffoldState = rememberScaffoldState()
            val snackbarController =
                SnackbarController(scaffoldState.snackbarHostState, lifecycleScope)

            PenguShopTheme {
                Scaffold(
                    bottomBar = {
                        if (loaded) {
                            AnimatedVisibility(visible = showBottomBar) {
                                BottomBar(navController)
                            }
                        }
                    },
                    scaffoldState = scaffoldState,
                    snackbarHost = {
                        scaffoldState.snackbarHostState
                    }
                ) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        loaded = true

                        composable("login") {
                            LoginScreen(navController, accountService, snackbarController)
                        }

                        composable("dashboard") {
                            DashboardScreen(navController, listsService, termiteService)
                        }

                        composable("setup") {
                            SetupScreen(navController)
                        }

                        composable("lists") {
                            ListsScreen(navController, listsService, storeState)
                        }

                        composable("new_list") {
                            NewList(navController, listsService, this@MainActivity, storeState)
                        }

                        composable("pantry_list") {
                            PantryList(navController, productsService, storeState)
                        }

                        composable("shopping_list") {
                            ShoppingList(navController, productsService, storeState)
                        }

                        composable("search") {
                            SearchScreen(navController, productsService, storeState)
                        }

                        composable("cart") {
                            CartScreen(navController, storeState)
                        }

                        composable("cart_confirmation") {
                            CartConfirmationScreen(navController, storeState)
                        }

                        composable("profile") {
                            ProfileScreen(
                                navController,
                                accountService,
                                snackbarController,
                                storeState
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(bottom = 8.dp + it.calculateBottomPadding())
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PenguSnackbar(snackbarHostState = scaffoldState.snackbarHostState)
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

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mReceiver)
    }

    /*
     * Termite listeners
     */
    override fun onPeersAvailable(peers: SimWifiP2pDeviceList) {
        val peersStr = StringBuilder()

        // compile list of devices in range
        for (device in peers.deviceList) {
            val dev = "${device.deviceName} (${device.virtIp})\n"
            peersStr.append(dev)
        }

        // display list of devices in range
        AlertDialog.Builder(this)
            .setTitle("Devices in WiFi Range")
            .setMessage(peersStr.toString())
            .setNeutralButton("Dismiss") { _, _ -> }
            .show()
    }
}