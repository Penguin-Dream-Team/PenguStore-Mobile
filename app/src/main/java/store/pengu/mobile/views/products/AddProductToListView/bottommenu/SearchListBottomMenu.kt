package store.pengu.mobile.views.products.AddProductToListView.bottommenu

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.maps.model.LatLng
import io.ktor.util.*
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.data.UserList
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.utils.Border
import store.pengu.mobile.utils.border
import store.pengu.mobile.utils.toColor
import store.pengu.mobile.views.lists.AvailableListColor
import store.pengu.mobile.views.maps.MapScreen
import store.pengu.mobile.views.partials.IconButton

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun <T : UserList> SearchListBottomMenu(
    listsService: ListsService,
    closeMenu: () -> Unit,
    title: String,
    onCreate: () -> Unit,
    onImport: () -> Unit,
    onSearch: (UserList) -> Unit,
    getLists: suspend () -> Unit,
    lists: SnapshotStateList<T>,
) {
    val context = LocalContext.current
    val lists = remember { lists }
    val coroutineScope = rememberCoroutineScope()
    var selectedList by remember { mutableStateOf(null as T?) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth()
    ) {
        coroutineScope.launch { getLists() }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { closeMenu() },
                icon = Icons.Filled.ArrowBack,
                description = "close search popup"
            )
            Text(
                text = stringResource(R.string.choose) + title.toLowerCase(Locale.current),
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1.0f, true))
            IconButton(
                onClick = { selectedList = null },
                icon = Icons.Filled.Delete,
                description = "clear search data",
                enabled = selectedList != null
            )
        }

        Divider(modifier = Modifier.padding(top = 2.dp, bottom = 10.dp))

        AnimatedVisibility (selectedList != null) {
            Text(
                text = stringResource(R.string.choose) + title.toLowerCase(Locale.current) + ":",
                modifier = Modifier
                    .padding(bottom = 3.dp)
                    .alpha(0.8f),
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            if (selectedList == null) {
                DropdownMenuItem(
                    onClick = { dropdownExpanded = true },
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.choose) + title.toLowerCase(Locale.current) + ":",
                        modifier = Modifier.weight(1.0f, true)
                    )

                    Surface {
                        AnimatedVisibility(
                            visible = dropdownExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Icon(imageVector = Icons.Filled.ArrowDropUp, "close pick list")
                        }

                        AnimatedVisibility(
                            visible = !dropdownExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Icon(imageVector = Icons.Filled.ArrowDropDown, "open pick list")
                        }
                    }
                }
            } else {
                DropdownMenuItem(
                    onClick = { dropdownExpanded = true },
                    modifier = Modifier
                        .border(
                            start = Border(
                                5.dp,
                                selectedList!!.color.toColor()
                            )
                        )
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = selectedList!!.name,
                        modifier = Modifier.weight(1.0f, true)
                    )

                    Surface {
                        AnimatedVisibility(
                            visible = dropdownExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Icon(imageVector = Icons.Filled.ArrowDropUp, "close pick list")
                        }

                        AnimatedVisibility(
                            visible = !dropdownExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Icon(imageVector = Icons.Filled.ArrowDropDown, "open pick list")
                        }
                    }
                }
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                lists.forEach { list ->
                    DropdownMenuItem(
                        onClick = {
                            selectedList = list
                            dropdownExpanded = false
                        },
                        modifier = Modifier
                            .border(
                                start = Border(
                                    5.dp,
                                    list.color.toColor()
                                )
                            )
                    ) {
                        Text(
                            text = list.name
                        )
                    }
                }
            }
        }

        Button(
            onClick = { onSearch(selectedList!!) }, enabled = selectedList != null,
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.add_to) + title)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Button(
                onClick = onCreate,
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.import_string_lower_case) + title,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(text = stringResource(R.string.create_without_space))
            }
            Button(
                onClick = onImport,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AddAPhoto,
                    contentDescription = stringResource(R.string.search_lower_case) + title,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(text = stringResource(R.string.import_without_space))
            }
        }
    }
}
