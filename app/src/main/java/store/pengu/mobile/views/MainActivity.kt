package store.pengu.mobile.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.theme.PenguShopTheme
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.cart.CartConfirmationScreen
import store.pengu.mobile.views.cart.CartScreen
import store.pengu.mobile.views.loading.LoadingScreen
import store.pengu.mobile.views.lists.ListsScreen
import store.pengu.mobile.views.lists.partials.NewList
import store.pengu.mobile.views.lists.partials.PantryList
import store.pengu.mobile.views.lists.partials.ShoppingList
import store.pengu.mobile.views.login.LoginScreen
import store.pengu.mobile.views.partials.BottomBar
import store.pengu.mobile.views.partials.BottomSheetMenus
import store.pengu.mobile.views.partials.FloatingActionButtons
import store.pengu.mobile.views.partials.PenguSnackbar
import store.pengu.mobile.views.profile.ProfileScreen
import store.pengu.mobile.views.search.SearchScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

    @ExperimentalMaterialApi
    private lateinit var bottomSheetState: BottomSheetState

    private var isBottomSheetMenuOpen: Boolean = false
    private lateinit var coroutineScope: CoroutineScope

    @ExperimentalMaterialApi
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
            val snackbarController =
                SnackbarController(scaffoldState.snackbarHostState, lifecycleScope)
            coroutineScope = rememberCoroutineScope()

            val buttonShape = CircleShape
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
            bottomSheetState = bottomSheetScaffoldState.bottomSheetState

            var executedOnce by remember { mutableStateOf(false) }
            if (!executedOnce) {
                navController.addOnDestinationChangedListener { _, _, _ ->
                    if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                        collapseBottomSheetMenu()
                    }
                }
                executedOnce = true
            }

            PenguShopTheme {
                BottomSheetScaffold(
                    scaffoldState = bottomSheetScaffoldState,
                    sheetContent = {
                        Box {
                            BottomSheetMenus(
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
                        snackbarHost = {
                            scaffoldState.snackbarHostState
                        },
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
                                    LoadingScreen(navController, listsService)
                                }

                                composable("lists") {
                                    ListsScreen(
                                        navController,
                                        listsService,
                                        storeState,
                                        snackbarController,
                                    )
                                }

                                composable("new_list") {
                                    NewList(
                                        navController,
                                        listsService,
                                        this@MainActivity,
                                        storeState
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
                                    .padding(bottom = 8.dp)
                                    .fillMaxSize()
                                    .zIndex(10f),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                PenguSnackbar(snackbarHostState = scaffoldState.snackbarHostState)
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
    fun expandBottomSheetMenu() {
        coroutineScope.launch {
            bottomSheetState.expand()
            isBottomSheetMenuOpen = true
        }
    }

    @ExperimentalMaterialApi
    fun collapseBottomSheetMenu() {
        coroutineScope.launch {
            bottomSheetState.collapse()
            isBottomSheetMenuOpen = false
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