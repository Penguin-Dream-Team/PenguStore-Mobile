package store.pengu.mobile.views.lists

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import store.pengu.mobile.R
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController


@ExperimentalMaterialApi
@Composable
fun AddProductToListFloatingActionButton(
    buttonShape: RoundedCornerShape,
    expandBottomSheetMenu: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            expandBottomSheetMenu()
        },
        backgroundColor = MaterialTheme.colors.primary,
        shape = buttonShape,
        contentColor = MaterialTheme.colors.onBackground
    ) {
        Icon(imageVector = Icons.Filled.Add, "Add item")
    }
}