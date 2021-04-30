package store.pengu.mobile.views.lists.bottommenu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import io.ktor.util.*
import kotlinx.coroutines.launch
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.CameraService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ListsBottomSheetMenu(
    listsService: ListsService,
    store: StoreState,
    snackbarController: SnackbarController,
    closeMenu: () -> Unit,
) {
    val selectedListType by remember { store.selectedListType }
    val coroutineScope = rememberCoroutineScope()

    val cameraService = remember { CameraService() }
    val (formType, setFormType) = remember { mutableStateOf(0) }

    val closeMenuWrapper = {
        if (formType == 2) {
            setFormType(1)
        }

        closeMenu()
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
                    coroutineScope.launch {
                        try {
                            listsService.createNewPantryList()
                            closeMenuWrapper()
                            snackbarController.showDismissibleSnackbar("Created new Pantry List")
                        } catch (e: PenguStoreApiException) {
                            snackbarController.showDismissibleSnackbar(e.message)
                        }
                    }
                },
                onImport = {
                    coroutineScope.launch {
                        try {
                            listsService.importNewPantryList()
                            closeMenuWrapper()
                            snackbarController.showDismissibleSnackbar("Imported new Pantry List")
                        } catch (e: PenguStoreApiException) {
                            snackbarController.showDismissibleSnackbar(e.message)
                        }
                    }
                },
                onScan = {
                    listsService.newListCode.value = it
                    setFormType(1)
                    snackbarController.showDismissibleSnackbar("Scanned $it")
                },
                snackbarController,
                cameraService
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
                    coroutineScope.launch {
                        try {
                            listsService.createNewShoppingList()
                            closeMenuWrapper()
                            snackbarController.showDismissibleSnackbar("Created new Shopping List")
                        } catch (e: PenguStoreApiException) {
                            snackbarController.showDismissibleSnackbar(e.message)
                        }
                    }
                },
                onImport = {
                    coroutineScope.launch {
                        try {
                            listsService.importNewShoppingList()
                            closeMenuWrapper()
                            snackbarController.showDismissibleSnackbar("Imported new Shopping List")
                        } catch (e: PenguStoreApiException) {
                            snackbarController.showDismissibleSnackbar(e.message)
                        }
                    }
                },
                onScan = {},
                snackbarController,
                cameraService
            )
        }
        else -> Unit
    }
}

@KtorExperimentalAPI
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ListBottomMenu(
    title: String,
    listsService: ListsService,
    closeMenu: () -> Unit,
    formType: Int,
    setFormType: (Int) -> Unit,
    onCreate: () -> Unit,
    onImport: () -> Unit,
    onScan: (String) -> Unit,
    snackbarController: SnackbarController,
    cameraService: CameraService
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
                cameraService
            )
    }
}
