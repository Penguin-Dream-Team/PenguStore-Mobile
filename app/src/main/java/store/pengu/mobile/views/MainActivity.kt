package store.pengu.mobile.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
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
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.*
import kotlinx.coroutines.*
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.services.*
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.WifiP2pBroadcastReceiver
import store.pengu.mobile.utils.camera.Camera
import store.pengu.mobile.views.cart.CartConfirmationScreen
import store.pengu.mobile.views.cart.CartScreen
import store.pengu.mobile.views.lists.ListsScreen
import store.pengu.mobile.views.lists.pantry.ViewPantryList
import store.pengu.mobile.views.lists.partials.ListView
import store.pengu.mobile.views.lists.partials.ShareListView
import store.pengu.mobile.views.lists.shops.ViewShoppingList
import store.pengu.mobile.views.loading.LoadingScreen
import store.pengu.mobile.views.login.LoginScreen
import store.pengu.mobile.views.partials.*
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
    lateinit var navController: NavHostController
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
    @SuppressLint("SourceLockedOrientationActivity", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        var loaded = false
        val startDestination: String

        // register broadcast receiver
        registerTermiteReceiver()

        runBlocking {
            accountService.loadData()
            startDestination = if (storeState.isLoggedIn()) {
                "loading"
            } else {
                "login"
            }
        }

        setContent {
            navController = rememberNavController()

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

                accountService.navController = navController

                termiteService.wifiDirectON()
                executedOnce = true
            }

            coroutineScope.launch {
                delay(200L)
                if (!storeState.isLoggedIn() && currentRoute != "login") {
                    navController.navigate("login")
                    navController.backStack.clear()
                }
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
                                currentRoute,
                                closeMenu = { collapseBottomSheetMenu() },
                            )
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

                                animatedComposable("login") {
                                    LoginScreen(navController, accountService, snackbarController)
                                }

                                animatedComposable("loading") {
                                    LoadingScreen(navController, listsService, snackbarController)
                                }

                                animatedComposable("lists") {
                                    ListsScreen(
                                        navController,
                                        listsService,
                                        storeState,
                                        snackbarController,
                                    )
                                }

                                animatedComposable("pantry_list") {
                                    ListView(
                                        navController,
                                        storeState,
                                        "share_pantry_list"
                                    ) {
                                        ViewPantryList(
                                            navController,
                                            productsService,
                                            storeState,
                                            it as PantryList
                                        )
                                    }
                                }

                                animatedComposable("share_pantry_list") {
                                    ShareListView(
                                        navController,
                                        storeState,
                                        snackbarController,
                                        "Pantry List"
                                    )
                                }

                                animatedComposable("share_shopping_list") {
                                    ShareListView(
                                        navController,
                                        storeState,
                                        snackbarController,
                                        "Shopping List"
                                    )
                                }

                                animatedComposable("shopping_list") {
                                    ListView(
                                        navController,
                                        storeState,
                                        "share_shopping_list"
                                    ) {
                                        ViewShoppingList(
                                            productsService,
                                            storeState,
                                            it as ShoppingList
                                        )
                                    }
                                }

                                animatedComposable("search") {
                                    SearchScreen(navController, productsService, storeState)
                                }

                                animatedComposable("product") {
                                    ProductScreen(
                                        navController,
                                        productsService,
                                        this@MainActivity,
                                        storeState
                                    )
                                }

                                animatedComposable("cart") {
                                    CartScreen(navController, storeState)
                                }

                                animatedComposable("cart_confirmation") {
                                    CartConfirmationScreen(navController, cartService, storeState)
                                }

                                animatedComposable("profile") {
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
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        coroutineScope.launch {
            bottomSheetState.expand()
            isBottomSheetMenuOpen = true
        }
    }

    @ExperimentalMaterialApi
    private fun collapseBottomSheetMenu() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        // uncomment to clear when popup is closed
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

    override fun onResume() {
        super.onResume()
        registerTermiteReceiver()
    }

    private fun registerTermiteReceiver() {
        val filter = IntentFilter()
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION)
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION)
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION)
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION)
        mReceiver = WifiP2pBroadcastReceiver(this, termiteService)
        registerReceiver(mReceiver, filter)
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