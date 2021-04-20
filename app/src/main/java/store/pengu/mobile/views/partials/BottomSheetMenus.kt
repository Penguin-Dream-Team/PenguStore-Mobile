package store.pengu.mobile.views.partials

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.lists.ListsBottomSheetMenu

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun BottomSheetMenus(
    listsService: ListsService,
    store: StoreState,
    productsService: ProductsService,
    snackbarController: SnackbarController,
    currentRoute: String?,
    closeMenu: () -> Unit
) {
    when (currentRoute) {
        "lists" ->
            ListsBottomSheetMenu(
                listsService,
                store,
                snackbarController,
                closeMenu
            )
        else -> Text("")
    }
}