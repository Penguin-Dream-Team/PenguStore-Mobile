package store.pengu.mobile.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import coil.ImageLoader
import com.google.android.gms.location.LocationSettingsStates
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.*
import kotlinx.coroutines.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener
import store.pengu.mobile.R
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.services.*
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.WifiP2pBroadcastReceiver
import store.pengu.mobile.views.cart.CartScreen
import store.pengu.mobile.views.lists.ListsScreen
import store.pengu.mobile.views.lists.pantry.ViewPantryList
import store.pengu.mobile.views.lists.partials.ListView
import store.pengu.mobile.views.lists.partials.ShareListView
import store.pengu.mobile.views.lists.shops.ViewShoppingList
import store.pengu.mobile.views.loading.LoadingScreen
import store.pengu.mobile.views.login.LoginScreen
import store.pengu.mobile.views.partials.*
import store.pengu.mobile.views.products.AddProductToListView.EditProductListsView
import store.pengu.mobile.views.products.NewProductView
import store.pengu.mobile.views.products.ProductInfo.ProductInfo
import store.pengu.mobile.views.profile.ProfileScreen
import store.pengu.mobile.views.search.SearchScreen
import store.pengu.mobile.views.termite.LocationTimer
import java.io.File
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

    @Inject
    lateinit var mapsService: MapsService

    private val termiteService = TermiteService(this)
    lateinit var navController: NavHostController
    private var mReceiver: WifiP2pBroadcastReceiver? = null

    @ExperimentalMaterialApi
    private lateinit var bottomSheetState: BottomSheetState

    private var isBottomSheetMenuOpen: Boolean = false
    private lateinit var coroutineScope: CoroutineScope

    private lateinit var imageLoader: ImageLoader

    private val enabledState = mutableStateOf(false)

    companion object {
        const val REQUEST_CHECK_SETTINGS = 0x1
    }

    @KtorExperimentalAPI
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @SuppressLint("SourceLockedOrientationActivity", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        var loaded = false
        val cameraService = CameraService()

        // register broadcast receiver
        registerTermiteReceiver()

        imageLoader = ImageLoader.Builder(applicationContext).apply {
            val CACHE_DIRECTORY_NAME = "image_cache"
            val CACHE_SIZE = 10L * 1024 * 1024 // MB
            val cacheDirectory =
                File(applicationContext.cacheDir, CACHE_DIRECTORY_NAME).apply { mkdirs() }
            val cache = Cache(cacheDirectory, CACHE_SIZE)
            okHttpClient {
                OkHttpClient.Builder()
                    .cache(cache)
                    .build()
            }
        }.build()

        setContent {
            navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
            Log.d("HELP", currentRoute ?: "")
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

                executedOnce = true
            }

            if (loaded && !storeState.isLoggedIn() && currentRoute != "loading" && currentRoute != "login") {
                navController.navigate("login")
                navController.backStack.clear()
            }

            var tryAgain by remember { mutableStateOf(false) }
            PenguShopTheme {
                LocationTimer(
                    mapsService,
                    storeState,
                    snackbarController,
                    termiteService,
                    this@MainActivity,
                    tryAgain,
                    enabledState.component2()
                )

                if (!enabledState.value) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            getString(R.string.noLocationAccess),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onBackground,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        Button(onClick = {
                            enabledState.value = false
                            tryAgain = !tryAgain
                        }) {
                            Text(getString(R.string.try_again))
                        }
                    }
                } else {

                    BottomSheetScaffold(
                        scaffoldState = bottomSheetScaffoldState,
                        snackbarHost = {
                            bottomSheetScaffoldState.snackbarHostState
                        },
                        sheetContent = {
                            Box {
                                BottomSheetMenus(
                                    imageLoader,
                                    listsService,
                                    productsService,
                                    storeState,
                                    snackbarController,
                                    navController,
                                    currentRoute,
                                    isBottomSheetMenuOpen,
                                    cameraService
                                ) { collapseBottomSheetMenu(it) }
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
                                    startDestination = "loading"
                                ) {
                                    loaded = true

                                    animatedComposable("login") {
                                        LoginScreen(
                                            navController,
                                            accountService,
                                            snackbarController
                                        )
                                    }

                                    animatedComposable("loading") {
                                        LoadingScreen(
                                            navController,
                                            listsService,
                                            snackbarController,
                                            mapsService,
                                            accountService,
                                            storeState
                                        )
                                    }

                                    animatedComposable("lists") {
                                        ListsScreen(
                                            navController,
                                            listsService,
                                            storeState,
                                            snackbarController,
                                        )
                                    }

                                    animatedComposable("pantry_list/{pantryId}", listOf(
                                        navArgument("pantryId") { type = NavType.LongType }
                                    )) { args ->
                                        ListView(
                                            navController,
                                            snackbarController,
                                            storeState,
                                            shareRoute = "share_pantry_list",
                                            listsService = listsService,
                                            listId = args!!["pantryId"] as Long,
                                            type = UserListType.PANTRY,
                                            mapsService = mapsService
                                        ) {
                                            ViewPantryList(
                                                navController,
                                                applicationContext,
                                                listsService,
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

                                    animatedComposable("shopping_list/{shopId}", listOf(
                                        navArgument("shopId") { type = NavType.LongType }
                                    )) { args ->
                                        ListView(
                                            navController,
                                            snackbarController,
                                            storeState,
                                            shareRoute = "share_shopping_list",
                                            listId = args!!["shopId"] as Long,
                                            type = UserListType.SHOPPING_LIST,
                                            listsService = listsService,
                                            mapsService = mapsService
                                        ) {
                                            ViewShoppingList(
                                                navController,
                                                snackbarController,
                                                productsService,
                                                storeState,
                                                it as ShoppingList
                                            )
                                        }
                                    }

                                    animatedComposable("search?shopId={shopId}&pantryId={pantryId}",
                                        listOf(
                                            navArgument("shopId") {
                                                nullable = true
                                            },
                                            navArgument("pantryId") {
                                                nullable = true
                                            }
                                        )
                                    ) { args ->
                                        SearchScreen(
                                            imageLoader,
                                            navController,
                                            productsService,
                                            storeState,
                                            args!!["shopId"].toString().toLongOrNull(),
                                            args["pantryId"].toString().toLongOrNull(),
                                        )
                                    }

                                    animatedComposable("product/{productId}", listOf(
                                        navArgument("productId") { type = NavType.LongType }
                                    )) { args ->
                                        ProductInfo(
                                            imageLoader,
                                            productsService,
                                            navController,
                                            storeState,
                                            args!!["productId"] as Long
                                        ) { expandBottomSheetMenu() }
                                    }

                                    animatedComposable("new_item?shopId={shopId}&pantryId={pantryId}",
                                        listOf(
                                            navArgument("shopId") {
                                                nullable = true
                                            },
                                            navArgument("pantryId") {
                                                nullable = true
                                            }
                                        )
                                    ) { args ->
                                        NewProductView(
                                            imageLoader,
                                            snackbarController,
                                            cameraService,
                                            productsService,
                                            args!!["shopId"].toString().toLongOrNull(),
                                            args["pantryId"].toString().toLongOrNull(),
                                            navController
                                        )
                                    }

                                    animatedComposable(
                                        "add_product_to_list/{productId}?listType={listType}&listId={listId}",
                                        listOf(
                                            navArgument("productId") { type = NavType.LongType },
                                            navArgument("listType") {
                                                defaultValue = UserListType.PANTRY.ordinal
                                            },
                                            navArgument("listId") {
                                                nullable = true
                                            }
                                        )
                                    ) { args ->
                                        EditProductListsView(
                                            imageLoader,
                                            snackbarController,
                                            listsService,
                                            productsService,
                                            storeState,
                                            UserListType.values()[args!!["listType"] as Int],
                                            args["productId"] as Long,
                                            navController,
                                            args["listId"].toString().toLongOrNull(),
                                        )
                                    }

                                    animatedComposable("cart") {
                                        CartScreen(
                                            snackbarController,
                                            navController,
                                            cartService,
                                            productsService,
                                            listsService,
                                            storeState
                                        )
                                    }

                                    animatedComposable("profile") {
                                        ProfileScreen(
                                            navController,
                                            accountService,
                                            snackbarController,
                                            applicationContext,
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
    private fun collapseBottomSheetMenu(destination: String? = null) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        coroutineScope.launch {
            bottomSheetState.collapse()
            isBottomSheetMenuOpen = false
            destination?.let {
                navController.navigate(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        termiteService.wifiDirectOFF()
        mReceiver?.let {
            unregisterReceiver(it)
        }
        mReceiver = null
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
        GlobalScope.launch {
            try {
                if (peers.deviceList.isEmpty()) {
                    beaconsService.leaveQueue(storeState.location)
                } else {
                    beaconsService.joinQueue(storeState.location)
                }
            } catch (e: Exception) {
                // ignore api exception
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                val states = LocationSettingsStates.fromIntent(intent)
                when (resultCode) {
                    Activity.RESULT_OK -> enabledState.value = true
                    Activity.RESULT_CANCELED -> {
                        // The user was asked to change settings, but chose not to
                        enabledState.value = false
                    }
                }
            }
        }
    }
}