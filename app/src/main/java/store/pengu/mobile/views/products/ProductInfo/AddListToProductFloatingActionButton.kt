package store.pengu.mobile.views.products.ProductInfo

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import store.pengu.mobile.R
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController


@ExperimentalMaterialApi
@Composable
fun ProductFloatingActionButton(
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
        Icon(imageVector = Icons.Filled.Edit, "Edit product")
    }
}