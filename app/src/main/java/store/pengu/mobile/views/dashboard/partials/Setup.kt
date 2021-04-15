package store.pengu.mobile.views.dashboard.partials

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate

@Composable
fun SetupScreen(navController: NavController) {

    Button(
        onClick = {
            navController.navigate("login")
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Login",
            textAlign = TextAlign.Center
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = {
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Continue as Guest",
            textAlign = TextAlign.Center
        )
    }
}