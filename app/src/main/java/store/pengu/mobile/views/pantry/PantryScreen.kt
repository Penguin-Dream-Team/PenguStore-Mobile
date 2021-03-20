package store.pengu.mobile.views.pantry

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate

@Composable
fun PantryScreen(navController: NavController) {

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Button(
            onClick = {
                navController.navigate("new_pantry")
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Create new Pantry",
                textAlign = TextAlign.Center
            )
        }

        // Available Pantries list
    }
}