package store.pengu.mobile.views.lists.bottommenu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import kotlinx.coroutines.launch
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.lists.bottommenu.CreateListBottomMenu

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

    when (selectedListType) {
        0 -> {
            CreateListBottomMenu(
                listsService,
                store,
                snackbarController,
                closeMenu,
                title = "Pantry List",
                onCreate = {
                    coroutineScope.launch {
                        try {
                            listsService.createNewPantryList()
                            closeMenu()
                            snackbarController.showDismissibleSnackbar("Created new Pantry List")
                        } catch (e: PenguStoreApiException) {
                            snackbarController.showDismissibleSnackbar(e.message)
                        }
                    }
                }
            )
        }
        1 -> {
            CreateListBottomMenu(
                listsService,
                store,
                snackbarController,
                closeMenu,
                title = "Shopping List",
                onCreate = {
                    coroutineScope.launch {
                        try {
                            listsService.createNewShoppingList()
                            closeMenu()
                            snackbarController.showDismissibleSnackbar("Created new Shopping List")
                        } catch (e: PenguStoreApiException) {
                            snackbarController.showDismissibleSnackbar(e.message)
                        }
                    }
                }
            )
        }
        else -> Unit
    }
}
