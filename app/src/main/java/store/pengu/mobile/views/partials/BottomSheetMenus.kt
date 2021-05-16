package store.pengu.mobile.views.partials

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import io.ktor.util.*
import store.pengu.mobile.services.CameraService
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.lists.bottommenu.ListsBottomSheetMenu
import store.pengu.mobile.views.lists.pantry.AddProductToPantryBottomMenu
import store.pengu.mobile.views.lists.shops.AddProductToShoppingListBottomMenu
import store.pengu.mobile.views.products.ProductInfo.bottommenu.EditProductBottomSheetMenu
import store.pengu.mobile.views.products.AddProductToListView.bottommenu.ListsBottomSheetMenu as AddProductToListBottomSheetMenu

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun BottomSheetMenus(
    listsService: ListsService,
    productsService: ProductsService,
    store: StoreState,
    snackbarController: SnackbarController,
    navController: NavController,
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
        "add_product_to_list/{productId}?listType={listType}&listId={listId}" ->
            AddProductToListBottomSheetMenu(
                listsService,
                productsService,
                store,
                snackbarController,
                navController,
                cameraService,
            ) { closeMenu(null) }
        "product/{productId}" ->
            EditProductBottomSheetMenu(
                productsService,
                store,
                snackbarController,
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