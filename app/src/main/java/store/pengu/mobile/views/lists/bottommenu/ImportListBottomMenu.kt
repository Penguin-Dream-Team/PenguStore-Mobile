package store.pengu.mobile.views.lists.bottommenu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import io.ktor.util.*
import store.pengu.mobile.R
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.views.partials.IconButton

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ImportListBottomMenu(
    listsService: ListsService,
    closeMenu: () -> Unit,
    title: String,
    onImport: () -> Unit,
    onCreate: () -> Unit,
    onScan: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var listCode by remember { listsService.newListCode }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { closeMenu() },
                icon = Icons.Filled.ArrowBack,
                description = "close import popup"
            )
            Text(
                text = stringResource(R.string.import_string) + title.toLowerCase(Locale.current),
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1.0f, true))
            IconButton(
                onClick = { listsService.resetImportListData() },
                icon = Icons.Filled.Delete,
                description = "clear import data",
                enabled = listsService.newCanImportList()
            )
        }

        Divider(modifier = Modifier.padding(top = 2.dp))

        OutlinedTextField(
            value = listCode,
            onValueChange = {
                listCode = it
            },
            placeholder = {
                Text(
                    title.toLowerCase(Locale.current).capitalize(Locale.current) + stringResource(R.string.code)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                onImport()
            }),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.QrCode, contentDescription = "list code")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
        )

        Text(
            text = stringResource(R.string.alternatively_scan_qr_code)
        )

        Button(
            onClick = onScan,
            modifier = Modifier
                .padding(top = 25.dp, bottom = 15.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.scan_qr_code))
        }

        Button(
            onClick = onImport, enabled = listsService.newCanImportList(),
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.import_string) + title)
        }

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

        Button(
            onClick = onCreate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Text(text = stringResource(R.string.create) + title)
        }
    }
}
