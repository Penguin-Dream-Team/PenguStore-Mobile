package store.pengu.mobile.views.lists

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.ui.res.stringResource
import store.pengu.mobile.R


@ExperimentalMaterialApi
@Composable
fun ListsFloatingActionButton(
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
        Icon(imageVector = Icons.Filled.PlaylistAdd, stringResource(R.string.create_list_button))
    }
}