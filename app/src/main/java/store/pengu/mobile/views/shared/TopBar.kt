package store.pengu.mobile.views.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun TopBar(navController: NavHostController) =
    TopAppBar(title = { Text("PenguStore") }, actions = {
        /*IconButton(
            onClick = {
                store.logout()
                navController.navigate("login")
            },
            icon = Icons.Outlined.ExitToApp,
            description = "Logout",
            selected = false
        )*/
    }, navigationIcon = {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            /*PenguLogo(
                modifier = Modifier
                    .preferredWidth(36.dp)
                    .preferredHeight(36.dp)
            )*/
        }
    })
