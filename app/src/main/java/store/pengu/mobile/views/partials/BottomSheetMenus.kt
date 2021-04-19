package store.pengu.mobile.views.partials

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.lists.ListsBottomSheetMenu

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun BottomSheetMenus(
    listsService: ListsService,
    store: StoreState,
    productsService: ProductsService,
    snackbarController: SnackbarController,
    currentRoute: String?
) {
    when (currentRoute) {
        "lists" ->
            ListsBottomSheetMenu(
                listsService,
                store,
                snackbarController
            )
        else -> Text("")
    }
}