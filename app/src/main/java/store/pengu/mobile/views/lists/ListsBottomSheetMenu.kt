package store.pengu.mobile.views.lists

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ListsBottomSheetMenu(
    listsService: ListsService,
    store: StoreState,
    snackbarController: SnackbarController,
) {
    val selectedListType by remember { store.selectedListType }
    when (selectedListType) {
        0 -> {
            PantryBottomSheetMenu(
                listsService,
                store,
                snackbarController,
            )
        }
        1 -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("Create shopping list")
            }
        }
        else -> Unit
    }
}
