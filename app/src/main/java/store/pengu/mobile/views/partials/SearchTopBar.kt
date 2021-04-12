package store.pengu.mobile.views.partials

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
fun SearchTopBar() {
    var query by remember { mutableStateOf("") }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val submit: (String) -> Unit = { it ->
        if (it.isNotBlank()) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        keyboardController?.hide()
    }

    TopAppBar(
        contentPadding = PaddingValues(8.dp),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxSize(),
            value = query,
            onValueChange = { newValue ->
                query = newValue
            },
            label = {
                Text(text = "Search")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, "Search Icon")
            },
            singleLine = true,
            trailingIcon = {
                Row {
                    if (query.isNotBlank()) {
                        IconButton(
                            onClick = {
                                query = ""
                            },
                            icon = Icons.Filled.Clear,
                            description = "Clear"
                        )
                    }
                    IconButton(
                        onClick = {
                            submit(query)
                        },
                        icon = Icons.Filled.ArrowForward,
                        description = "Search",
                        selected = query.isNotBlank()
                    )
                }
            },
            keyboardActions = KeyboardActions(onSearch = {
                submit(query)
            })
        )
    }
}
