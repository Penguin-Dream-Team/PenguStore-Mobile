package store.pengu.mobile.views.partials

import androidx.compose.animation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import store.pengu.mobile.views.lists.AddProductToListFloatingActionButton
import store.pengu.mobile.views.lists.ListsFloatingActionButton
import store.pengu.mobile.views.products.ProductInfo.ProductFloatingActionButton

@ExperimentalAnimationApi
@Composable
fun FloatingButton(
    currentRoute: String?,
    expectedRoute: String? = null,
    expectedRoutes: List<String> = listOf(expectedRoute ?: ""),
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = expectedRoutes.contains(currentRoute),
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
    expandBottomSheetMenu: () -> Unit,
    currentRoute: String?
) {
    FloatingButton(currentRoute, expectedRoute = "lists") {
        ListsFloatingActionButton(
            buttonShape,
            expandBottomSheetMenu
        )
    }

    FloatingButton(
        currentRoute, expectedRoutes = listOf(
            "pantry_list/{pantryId}",
            "shopping_list/{shopId}",
        )
    ) {
        AddProductToListFloatingActionButton(
            buttonShape,
            expandBottomSheetMenu
        )
    }

    FloatingButton(
        currentRoute,
        expectedRoute = "add_product_to_list/{productId}?listType={listType}&listId={listId}"
    ) {
        ListsFloatingActionButton(
            buttonShape,
            expandBottomSheetMenu
        )
    }

    FloatingButton(
        currentRoute,
        expectedRoute = "product/{productId}"
    ) {
        ProductFloatingActionButton(
            buttonShape,
            expandBottomSheetMenu
        )
    }

}