package store.pengu.mobile.views.pantry.partials

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.maps.MapScreen

@Composable
fun NewPantry(navController: NavController, supportFragmentManager: FragmentManager) {
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

        /*Box(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colors.onSurface)
        ) {
            MapScreen(navController, store)
        }*/
        MapScreen(supportFragmentManager)

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