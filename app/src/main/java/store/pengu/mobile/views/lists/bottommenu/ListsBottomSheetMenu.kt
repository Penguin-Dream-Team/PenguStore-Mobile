package store.pengu.mobile.views.lists.bottommenu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import io.ktor.util.*
import kotlinx.coroutines.launch
import store.pengu.mobile.R
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
    cameraService: CameraService,
    closeMenu: () -> Unit,
) {
    val selectedListType by remember { store.selectedListType }
    val coroutineScope = rememberCoroutineScope()

    val createdNewPantryList = stringResource(R.string.created_new_pantry_list)
    val importedNewPantryList = stringResource(R.string.imported_new_pantry_list)
    val scanned = stringResource(R.string.scanned)

    val (formType, setFormType) = remember { mutableStateOf(0) }

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

    when (selectedListType) {
        0 -> {
            ListBottomMenu(
                title = stringResource(R.string.pantry_list),
                listsService,
                closeMenuWrapper,
                formType,
                setFormType,
                onCreate = {
                    actionWrapper(createdNewPantryList) {
                        listsService.createNewPantryList()
                    }
                },
                onImport = {
                    actionWrapper(importedNewPantryList) {
                        listsService.importNewPantryList()
                    }
                },
                onScan = {
                    listsService.newListCode.value = it
                    setFormType(1)
                    snackbarController.showDismissibleSnackbar(scanned + it)
                },
                snackbarController,
                cameraService
            )
        }
        1 -> {
            ListBottomMenu(
                title = stringResource(R.string.shopping_list),
                listsService,
                closeMenuWrapper,
                formType,
                setFormType,
                onCreate = {
                    actionWrapper(createdNewPantryList) {
                        listsService.createNewShoppingList()
                    }
                },
                onImport = {
                    actionWrapper(importedNewPantryList) {
                        listsService.importNewShoppingList()
                    }
                },
                onScan = {
                    listsService.newListCode.value = it
                    setFormType(1)
                    snackbarController.showDismissibleSnackbar(scanned + it)
                },
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
