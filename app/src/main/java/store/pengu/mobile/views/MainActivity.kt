package store.pengu.mobile.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.runBlocking
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.services.*
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.cart.CartConfirmationScreen
import store.pengu.mobile.views.cart.CartScreen
import store.pengu.mobile.views.loading.LoadingScreen
import store.pengu.mobile.views.lists.ListsScreen
import store.pengu.mobile.views.lists.partials.PantryList
import store.pengu.mobile.views.lists.partials.ShoppingList
import store.pengu.mobile.views.login.LoginScreen
import store.pengu.mobile.views.partials.BottomBar
import store.pengu.mobile.views.partials.BottomSheetMenus
import store.pengu.mobile.views.partials.FloatingActionButtons
import store.pengu.mobile.views.partials.PenguSnackbar
import store.pengu.mobile.utils.WifiP2pBroadcastReceiver
import store.pengu.mobile.utils.camera.Camera
import store.pengu.mobile.views.profile.ProfileScreen
import store.pengu.mobile.views.search.SearchScreen
import store.pengu.mobile.views.search.partials.ProductScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PeerListListener {

    @Inject
    lateinit var accountService: AccountService

    @Inject
    lateinit var beaconsService: BeaconsService

    @Inject
    lateinit var cartService: CartService

    @Inject
    lateinit var listsService: ListsService

    @Inject
    lateinit var productsService: ProductsService

    @Inject
    lateinit var storeState: StoreState

    @Inject
    lateinit var api: PenguStoreApi

    private val termiteService = TermiteService(this)
    private var navController: NavHostController? = null
    private var mReceiver: WifiP2pBroadcastReceiver? = null

    @ExperimentalMaterialApi
    private lateinit var bottomSheetState: BottomSheetState

    private var isBottomSheetMenuOpen: Boolean = false
    private lateinit var coroutineScope: CoroutineScope

    @KtorExperimentalAPI
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        var loaded = false
        val startDestination: String

        // register broadcast receiver
        val filter = IntentFilter()
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION)
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION)
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION)
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION)
        mReceiver = WifiP2pBroadcastReceiver(this, termiteService)
        registerReceiver(mReceiver, filter)

        requestPermission(Manifest.permission.CAMERA)

        runBlocking {
            accountService.loadData()
            startDestination = if (storeState.isLoggedIn()) {
                "loading"
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
            val noBottomBarRoutes = listOf("login", "loading")
            val showBottomBar = !noBottomBarRoutes.contains(currentRoute)

            val scaffoldState = rememberScaffoldState()
            coroutineScope = rememberCoroutineScope()

            val buttonShape = CircleShape
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
            bottomSheetState = bottomSheetScaffoldState.bottomSheetState
            val snackbarController =
                SnackbarController(bottomSheetScaffoldState.snackbarHostState, lifecycleScope)

            var executedOnce by remember { mutableStateOf(false) }
            if (!executedOnce) {
                navController.addOnDestinationChangedListener { _, _, _ ->
                    if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                        collapseBottomSheetMenu()
                    }
                }
                termiteService.wifiDirectON()
                executedOnce = true
            }

            PenguShopTheme {
                BottomSheetScaffold(
                    scaffoldState = bottomSheetScaffoldState,
                    snackbarHost = {
                        bottomSheetScaffoldState.snackbarHostState
                    },
                    sheetContent = {
                        Box {
                            BottomSheetMenus(
                                navController,
                                listsService,
                                storeState,
                                productsService,
                                snackbarController,
                                currentRoute
                            ) { collapseBottomSheetMenu() }
                        }
                    },
                    sheetPeekHeight = 0.dp,
                    sheetGesturesEnabled = true,
                ) {
                    Scaffold(
                        bottomBar = {
                            if (loaded) {
                                AnimatedVisibility(visible = showBottomBar) {
                                    BottomBar(navController, buttonShape)
                                }
                            }
                        },
                        scaffoldState = scaffoldState,
                        isFloatingActionButtonDocked = true,
                        floatingActionButton = {
                            FloatingActionButtons(
                                buttonShape,
                                listsService,
                                storeState,
                                productsService,
                                snackbarController,
                                { expandBottomSheetMenu() },
                                currentRoute,
                            )
                        },
                        floatingActionButtonPosition = FabPosition.Center
                    ) { paddingValues ->
                        Surface(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize(),
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = startDestination
                            ) {
                                loaded = true

                                composable("login") {
                                    LoginScreen(navController, accountService, snackbarController)
                                }

                                composable("loading") {
                                    LoadingScreen(navController, listsService, snackbarController)
                                }

                                composable("lists") {
                                    ListsScreen(
                                        navController,
                                        listsService,
                                        storeState,
                                        snackbarController,
                                    )
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

                                composable("product") {
                                    ProductScreen(navController, this@MainActivity, storeState)
                                }

                                composable("cart") {
                                    CartScreen(navController, storeState)
                                }

                                composable("cart_confirmation") {
                                    CartConfirmationScreen(navController, cartService, storeState)
                                }

                                composable("profile") {
                                    ProfileScreen(
                                        navController,
                                        accountService,
                                        snackbarController,
                                        storeState
                                    )
                                }

                                composable("camera") {
                                    Camera().CameraPreview(
                                        navController,
                                        storeState,
                                        productsService
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .fillMaxSize()
                                    .zIndex(1000f),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                PenguSnackbar(snackbarHostState = bottomSheetScaffoldState.snackbarHostState)
                            }
                        }
                    }
                }
            }
        }
    }

    @ExperimentalMaterialApi
    override fun onBackPressed() {
        if (isBottomSheetMenuOpen) {
            collapseBottomSheetMenu()
        } else {
            super.onBackPressed()
        }
    }

    @ExperimentalMaterialApi
    private fun expandBottomSheetMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        coroutineScope.launch {
            bottomSheetState.expand()
            isBottomSheetMenuOpen = true
        }
    }

    @ExperimentalMaterialApi
    private fun collapseBottomSheetMenu() {
        // uncomment to clear when popup is closed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        //listsService.resetNewListData()
        coroutineScope.launch {
            bottomSheetState.collapse()
            isBottomSheetMenuOpen = false
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mReceiver)
    }

    private fun requestPermission(permission: String) {
        if (PermissionChecker.checkSelfPermission(
                applicationContext,
                permission
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 100)
        }
    }

    /*
     * Termite listeners
     */
    override fun onPeersAvailable(peers: SimWifiP2pDeviceList) {
        if (peers.deviceList.isEmpty())
            beaconsService.leaveQueue()
        else
            beaconsService.joinQueue()

        /*val peersStr = StringBuilder()
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
            .show()*/
    }
}