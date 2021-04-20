package store.pengu.mobile.views.lists

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Label
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.Border
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.border
import store.pengu.mobile.views.maps.MapScreen
import store.pengu.mobile.views.partials.IconButton

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ShopsBottomSheetMenu(
    listsService: ListsService,
    store: StoreState,
    snackbarController: SnackbarController,
    closeMenu: () -> Unit,
    nameState: MutableState<String>,
    locationState: MutableState<LatLng?>,
    selectedColorState: MutableState<AvailableListColor>,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var shoppingListName by nameState
    var location by locationState
    var selectedColor by selectedColorState
    var canCreate by remember { mutableStateOf(false) }
    var canPickLocation by remember { mutableStateOf(false) }
    var colorExpanded by remember { mutableStateOf(false) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            with(it) {
                if (resultCode == RESULT_OK) {
                    val lat = data?.getDoubleExtra("LATITUDE", 0.0) ?: 0.0
                    val long = data?.getDoubleExtra("LONGITUDE", 0.0) ?: 0.0
                    location = LatLng(lat, long)
                    Toast.makeText(context, "Chosen location at $lat, $long", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    canPickLocation = shoppingListName.isNotBlank()
    canCreate = canPickLocation && location != null

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
                text = "Create shopping list",
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
        Divider(modifier = Modifier.padding(top = 2.dp))

        OutlinedTextField(
            value = shoppingListName,
            onValueChange = {
                shoppingListName = it
            },
            placeholder = { Text("Shopping list name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                keyboardController?.hide()
            }),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Label, contentDescription = "shopping name")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
        )

        Text(
            text = "Choose shopping list color:",
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
                    putExtra("NAME", shoppingListName)
                    putExtra("HAS_LOCATION", location != null)
                    putExtra("LATITUDE", location?.latitude ?: 0.0)
                    putExtra("LONGITUDE", location?.longitude ?: 0.0)
                })
            }, enabled = canPickLocation,
            modifier = Modifier
                .padding(vertical = 25.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Pick Location")
        }

        Button(
            onClick = {
            }, enabled = canCreate,
            modifier = Modifier
                .padding(bottom = 25.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Create Shopping List")
        }
    }
}