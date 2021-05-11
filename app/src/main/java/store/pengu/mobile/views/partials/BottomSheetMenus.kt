package store.pengu.mobile.views.partials

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import io.ktor.util.*
import store.pengu.mobile.services.CameraService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.lists.bottommenu.ListsBottomSheetMenu
import store.pengu.mobile.views.lists.pantry.AddProductToPantryBottomMenu
import store.pengu.mobile.views.lists.shops.AddProductToShoppingListBottomMenu

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun BottomSheetMenus(
    listsService: ListsService,
    store: StoreState,
    snackbarController: SnackbarController,
    currentRoute: String?,
    isBottomSheetOpen: Boolean,
    cameraService: CameraService,
    closeMenu: (String?) -> Unit,
) {
    when (currentRoute) {
        "lists" ->
            ListsBottomSheetMenu(
                listsService,
                store,
                snackbarController,
                cameraService
            ) { closeMenu(null) }
        "pantry_list/{pantryId}" ->
            AddProductToPantryBottomMenu(
                store,
                closeMenu
            )
        "shopping_list/{shopId}" ->
            AddProductToShoppingListBottomMenu(
                store,
                closeMenu
            )
        else -> {
            if (isBottomSheetOpen) {
                closeMenu(null)
            }
            Text("")
        }
    }
}