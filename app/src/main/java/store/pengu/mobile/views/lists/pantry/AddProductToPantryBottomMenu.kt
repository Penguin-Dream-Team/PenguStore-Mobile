package store.pengu.mobile.views.lists.pantry

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.ktor.util.*
import store.pengu.mobile.R
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.partials.IconButton

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun AddProductToPantryBottomMenu(
    store: StoreState,
    closeMenu: (String?) -> Unit
) {
    val selectedList = remember { store.selectedList }
    if (selectedList == null) {
        closeMenu("lists")
        return
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { closeMenu(null) },
                icon = Icons.Filled.ArrowBack,
                description = "close create popup"
            )
            Text(
                text = stringResource(R.string.add_product),
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold
            )
        }

        Divider(modifier = Modifier.padding(top = 2.dp, bottom = 25.dp))

        IconTextButton(
            label = stringResource(R.string.find_product),
            icon = Icons.Filled.Search,
            onClick = {
                closeMenu("search?pantryId=${selectedList.id}")
            }
        )

        OrDivider()

        IconTextButton(
            label = stringResource(R.string.create_new_product),
            icon = Icons.Filled.Add,
            onClick = {
                closeMenu("new_item?pantryId=${selectedList.id}")
            }
        )
/*

        OrDivider()

        IconTextButton(
            label = stringResource(R.string.scan_barcode),
            icon = Icons.Filled.PhotoCamera,
            onClick = {
                closeMenu("import_barcode")
            }
        )
*/
    }
}

@Composable
private fun OrDivider() {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .weight(0.5f, true)
        )
        Text(
            text = stringResource(R.string.or),
            modifier = Modifier
                .weight(0.2f, true),
            textAlign = TextAlign.Center
        )
        Divider(
            modifier = Modifier
                .weight(0.5f, true)
        )
    }
}

@Composable
private fun IconTextButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.padding(end = 5.dp)
        )
        Text(text = label)
    }
}

