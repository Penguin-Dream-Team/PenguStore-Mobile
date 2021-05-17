package store.pengu.mobile.views.products.AddProductToListView.bottommenu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import io.ktor.util.*
import kotlinx.coroutines.launch
import store.pengu.mobile.data.PantryList
import store.pengu.mobile.data.Product
import store.pengu.mobile.data.ShoppingList
import store.pengu.mobile.data.UserList
import store.pengu.mobile.data.productlists.ProductListEntry
import store.pengu.mobile.data.productlists.ProductPantryListEntry
import store.pengu.mobile.data.productlists.ProductShoppingListEntry
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.CameraService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.products.AddProductToListView.PantryListDialog
import store.pengu.mobile.views.products.AddProductToListView.ShoppingListDialog
import store.pengu.mobile.views.products.partials.Suggestions

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ListsBottomSheetMenu(
    listsService: ListsService,
    productsService: ProductsService,
    store: StoreState,
    snackbarController: SnackbarController,
    navController: NavController,
    cameraService: CameraService,
    closeMenu: () -> Unit,
) {
    val selectedListType by remember { store.selectedListType }
    val coroutineScope = rememberCoroutineScope()

    val (formType, setFormTypeWrapper) = remember { mutableStateOf(3) }

    var selectedList by remember { mutableStateOf(null as ProductListEntry?) }
    var showDialog by remember { mutableStateOf(false) }
    val product = store.selectedProduct

    val (showSuggestion, setShowSuggestion) = remember { mutableStateOf(false) }
    var suggestion: Product? by remember{ mutableStateOf(null) }

    val setFormType: (Int) -> Unit = {
        selectedList = null
        showDialog = false
        setFormTypeWrapper(it)
    }

    val closeMenuWrapper = {
        if (formType == 2) {
            setFormType(1)
        }

        closeMenu()
    }

    val actionWrapper: (String, suspend () -> Unit) -> Unit = { successMessage, callable ->
        coroutineScope.launch {
            try {
                callable()
                snackbarController.showDismissibleSnackbar(successMessage)
            } catch (e: PenguStoreApiException) {
                snackbarController.showDismissibleSnackbar(e.message)
            }
            closeMenuWrapper()
        }
    }

    val openPopup: (UserList) -> Unit = {
        selectedList = when (it) {
            is PantryList -> ProductPantryListEntry(
                it.id,
                it.name,
                it.color,
                0,
                0,
                it.shared,
                it.latitude,
                it.longitude
            )
            is ShoppingList -> ProductShoppingListEntry(
                it.id,
                it.name,
                it.color,
                null,
                it.latitude,
                it.longitude
            )
            else -> null
        }
        showDialog = true
        closeMenuWrapper()
    }

    when (selectedListType) {
        0 -> {
            ListBottomMenu(
                title = "Pantry List",
                listsService,
                closeMenuWrapper,
                formType,
                setFormType,
                onCreate = {
                    actionWrapper("Created new Pantry List") {
                        val list = listsService.createNewPantryList()
                        openPopup(list)
                        closeMenuWrapper()
                    }
                },
                onImport = {
                    actionWrapper("Imported new Pantry List") {
                        val list = listsService.importNewPantryList()
                        openPopup(list)
                        closeMenuWrapper()
                    }
                },
                onScan = {
                    listsService.newListCode.value = it
                    setFormType(1)
                    snackbarController.showDismissibleSnackbar("Scanned $it")
                },
                onSearch = {
                    openPopup(it)
                    closeMenuWrapper()
                },
                snackbarController,
                cameraService,
                { listsService.getPantryLists() },
                listsService.pantryLists
            )
        }
        1 -> {
            ListBottomMenu(
                title = "Shopping List",
                listsService,
                closeMenuWrapper,
                formType,
                setFormType,
                onCreate = {
                    actionWrapper("Created new Shopping List") {
                        val list = listsService.createNewShoppingList()
                        openPopup(list)
                        closeMenuWrapper()
                    }
                },
                onImport = {
                    actionWrapper("Imported new Shopping List") {
                        val list = listsService.importNewShoppingList()
                        openPopup(list)
                        closeMenuWrapper()
                    }
                },
                onScan = {},
                onSearch = {
                    openPopup(it as ShoppingList)
                    closeMenuWrapper()
                },
                snackbarController,
                cameraService,
                { listsService.getShoppingLists() },
                listsService.shoppingLists
            )
        }
        else -> Unit
    }

    if (showDialog && selectedList != null) {
        when (selectedListType) {
            0 -> (selectedList as ProductPantryListEntry).let {
                val (haveAmount, setHaveAmount) = remember { mutableStateOf(it.amountAvailable) }
                val (needAmount, setNeedAmount) = remember { mutableStateOf(it.amountNeeded) }
                PantryListDialog(
                    it.listName,
                    product = product?.toProductInPantry(
                        it.listId,
                        it.amountAvailable,
                        it.amountNeeded
                    ),
                    haveAmount = haveAmount,
                    needAmount = needAmount,
                    setHaveAmount = setHaveAmount,
                    setNeedAmount = setNeedAmount,
                    onClose = {
                        showDialog = false
                    },
                    onSave = {
                        coroutineScope.launch {
                            suggestion = productsService.addProductToPantryList(
                                product!!.id,
                                product.barcode,
                                it.listId,
                                haveAmount,
                                needAmount
                            )
                            setShowSuggestion(true)
                            showDialog = false
                        }
                    }
                )
            }
            1 -> (selectedList as ProductShoppingListEntry).let {
                val (price, setPrice) = remember { mutableStateOf(it.price) }
                ShoppingListDialog(
                    it.listName,
                    product = it,
                    price = price,
                    setPrice = setPrice,
                    onClose = {
                        showDialog = false
                    },
                    onSave = {
                        coroutineScope.launch {
                            productsService.addProductToShoppingList(
                                product!!.id,
                                it.listId,
                                price ?: 0.0
                            )
                            showDialog = false
                        }
                    },
                )
            }
        }
    }

    if (showSuggestion) {
        if (suggestion != null && selectedList != null)
            Suggestions(navController, suggestion!!, selectedList!!.listId, setShowSuggestion)
        else setShowSuggestion(false)
    }
}

@KtorExperimentalAPI
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun <T : UserList> ListBottomMenu(
    title: String,
    listsService: ListsService,
    closeMenu: () -> Unit,
    formType: Int,
    setFormType: (Int) -> Unit,
    onCreate: () -> Unit,
    onImport: () -> Unit,
    onScan: (String) -> Unit,
    onSearch: (UserList) -> Unit,
    snackbarController: SnackbarController,
    cameraService: CameraService,
    getLists: suspend () -> Unit,
    lists: SnapshotStateList<T>
) {
    when (formType) {
        0 ->
            CreateListBottomMenu(
                listsService,
                closeMenu,
                title,
                onCreate,
                onImport = {
                    setFormType(1)
                },
                onSearch = {
                    setFormType(3)
                }
            )
        1 ->
            ImportListBottomMenu(
                listsService,
                closeMenu,
                title,
                onImport,
                onCreate = {
                    setFormType(0)
                },
                onScan = {
                    setFormType(2)
                },
                onSearch = {
                    setFormType(3)
                }
            )
        2 ->
            ScanListBottomMenu(
                goBack = {
                    setFormType(1)
                },
                title,
                onScan,
                snackbarController,
                cameraService,
                onSearch = {
                    setFormType(3)
                }
            )
        3 ->
            SearchListBottomMenu(
                listsService,
                closeMenu,
                title,
                onCreate = {
                    setFormType(0)
                },
                onImport = {
                    setFormType(1)
                },
                onSearch = onSearch,
                getLists = getLists,
                lists
            )
    }
}
