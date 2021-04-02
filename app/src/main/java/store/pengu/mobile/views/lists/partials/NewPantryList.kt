package store.pengu.mobile.views.lists.partials

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.views.maps.MapScreen

@Composable
fun NewPantryList(navController: NavController, context: Context) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Pantry Name") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, MapScreen::class.java))
            }) {
            Text("Pick a Location")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            enabled = text != "",
            onClick = {
                navController.navigate("search")
            }) {
            Text("Continue")
        }
    }
}