package store.pengu.mobile.views.lists.bottommenu

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
import store.pengu.mobile.R
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.utils.Border
import store.pengu.mobile.utils.border
import store.pengu.mobile.views.lists.AvailableListColor
import store.pengu.mobile.views.maps.MapScreen
import store.pengu.mobile.views.partials.IconButton

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun CreateListBottomMenu(
    listsService: ListsService,
    closeMenu: () -> Unit,
    title: String,
    onCreate: () -> Unit,
    onImport: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    var listName by remember { listsService.newListName }
    var location by remember { listsService.newListLocation }
    var selectedColor by remember { listsService.newListColor }

    var colorExpanded by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            with(it) {
                if (resultCode == RESULT_OK) {
                    val lat = data?.getDoubleExtra("LATITUDE", 0.0) ?: 0.0
                    val long = data?.getDoubleExtra("LONGITUDE", 0.0) ?: 0.0
                    location = LatLng(lat, long)
                    /*Toast.makeText(context, "Chosen location at $lat, $long", Toast.LENGTH_SHORT)
                        .show()*/
                }
            }
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
                onClick = { closeMenu() },
                icon = Icons.Filled.ArrowBack,
                description = "close create popup"
            )
            Text(
                text = stringResource(R.string.create) + title.toLowerCase(Locale.current),
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1.0f, true))
            IconButton(
                onClick = { listsService.resetNewListData() },
                icon = Icons.Filled.Delete,
                description = "clear create data",
                enabled = listsService.newCanPickLocation()
            )
        }

        Divider(modifier = Modifier.padding(top = 2.dp))

        OutlinedTextField(
            value = listName,
            onValueChange = {
                listName = it
            },
            placeholder = {
                Text(
                    title.toLowerCase(Locale.current).capitalize(Locale.current) + stringResource(R.string.name)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                keyboardController?.hide()
            }),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Label, contentDescription = "list name")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
        )

        Text(
            text = stringResource(R.string.choose) + title.toLowerCase(Locale.current) + stringResource(R.string.color),
            modifier = Modifier
                .padding(bottom = 3.dp)
                .alpha(0.8f),
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            DropdownMenuItem(
                onClick = { colorExpanded = true },
                modifier = Modifier
                    .border(
                        start = Border(
                            5.dp,
                            selectedColor.toColor()
                        )
                    )
                    .fillMaxWidth(),
            ) {
                Text(
                    text = selectedColor.getName(),
                    modifier = Modifier.weight(1.0f, true)
                )

                Surface {
                    AnimatedVisibility(
                        visible = colorExpanded,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowDropUp, "close pick color")
                    }

                    AnimatedVisibility(
                        visible = !colorExpanded,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowDropDown, "open pick color")
                    }
                }
            }

            DropdownMenu(
                expanded = colorExpanded,
                onDismissRequest = { colorExpanded = false },
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                AvailableListColor.values().forEach { color ->
                    val colorValue = color.toColor()
                    DropdownMenuItem(
                        onClick = {
                            selectedColor = color
                            colorExpanded = false
                        },
                        modifier = Modifier
                            .border(
                                start = Border(
                                    5.dp,
                                    colorValue
                                )
                            )
                    ) {
                        Text(
                            text = color.getName(),
                            color = colorValue
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                launcher.launch(Intent(context, MapScreen::class.java).apply {
                    putExtra("NAME", listName)
                    putExtra("HAS_LOCATION", location != null)
                    putExtra("LATITUDE", location?.latitude ?: 0.0)
                    putExtra("LONGITUDE", location?.longitude ?: 0.0)
                })
            }, enabled = listsService.newCanPickLocation(),
            modifier = Modifier
                .padding(top = 25.dp, bottom = 15.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.pick_location))
        }

        Button(
            onClick = onCreate, enabled = listsService.newCanCreate(),
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.create) + title)
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
            onClick = onImport,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Text(text = stringResource(R.string.import_string) + title)
        }
    }
}
