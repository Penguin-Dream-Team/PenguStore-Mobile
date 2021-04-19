package store.pengu.mobile.views.lists

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.rememberCoroutineScope
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController


@ExperimentalMaterialApi
@Composable
fun ListsFloatingActionButton(
    buttonShape: RoundedCornerShape,
    listsService: ListsService,
    storeState: StoreState,
    snackbarController: SnackbarController,
    expandBottomSheetMenu: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    FloatingActionButton(
        onClick = {
            expandBottomSheetMenu()
        },
        backgroundColor = MaterialTheme.colors.primary,
        shape = buttonShape,
        contentColor = MaterialTheme.colors.onBackground
    ) {
        Icon(imageVector = Icons.Filled.Add, "create list")
    }
}