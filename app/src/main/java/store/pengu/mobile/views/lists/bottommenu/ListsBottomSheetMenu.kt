package store.pengu.mobile.views.lists

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.android.gms.maps.model.LatLng
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController

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

    val shoppingListName = remember { mutableStateOf("") }
    val location: MutableState<LatLng?> = remember { mutableStateOf(null) }
    val selectedColor = remember { mutableStateOf(AvailableListColor.BLUE) }

    when (selectedListType) {
        0 -> {
            PantryBottomSheetMenu(
                listsService,
                store,
                snackbarController,
                closeMenu,
                shoppingListName,
                location,
                selectedColor
            )
        }
        1 -> {
            ShopsBottomSheetMenu(
                listsService,
                store,
                snackbarController,
                closeMenu,
                shoppingListName,
                location,
                selectedColor
            )
        }
        else -> Unit
    }
}
