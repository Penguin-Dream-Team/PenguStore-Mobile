package store.pengu.mobile.views.partials

import androidx.compose.animation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.lists.ListsFloatingActionButton

@ExperimentalAnimationApi
@Composable
fun FloatingButton(
    currentRoute: String?,
    expectedRoute: String,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = currentRoute == expectedRoute,
        enter = fadeIn() + slideIn({ IntOffset(0, it.height / 2) }),
        exit = fadeOut() + slideOut({ IntOffset(0, it.height / 2) }),
        initiallyVisible = false
    ) {
        content()
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun FloatingActionButtons(
    buttonShape: RoundedCornerShape,
    listsService: ListsService,
    storeState: StoreState,
    productsService: ProductsService,
    snackbarController: SnackbarController,
    expandBottomSheetMenu: () -> Unit,
    currentRoute: String?
) {
    FloatingButton(currentRoute, "lists") {
        ListsFloatingActionButton(
            buttonShape,
            listsService,
            storeState,
            snackbarController,
            expandBottomSheetMenu
        )
    }
}