package store.pengu.mobile.views.lists.partials

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.maps.MapScreen

@Composable
fun NewList(
    navController: NavController,
    listsService: ListsService,
    activity: Activity,
    store: StoreState
) {
    var listName by remember { mutableStateOf("") }
    val label = if (store.listType == 0) "Pantry List Name" else "Shopping List Name"

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = listName,
            onValueChange = { listName = it },
            label = { Text(label) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                startActivityForResult(activity, Intent(activity, MapScreen::class.java), 111, null)
            }) {
            Text("Pick a Location")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            enabled = listName != "",
            onClick = {
                listsService.createList(listName)
                navController.navigate("search")
            }) {
            Text("Continue")
        }
    }
}