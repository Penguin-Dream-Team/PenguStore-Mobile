package store.pengu.mobile.views.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.partials.IconButton


@ExperimentalAnimationApi
@Composable
fun ProfileScreenInformation(
    navController: NavController,
    accountService: AccountService,
    snackbarController: SnackbarController,
    store: StoreState,
    coroutineScope: CoroutineScope
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "User Information",
            fontWeight = MaterialTheme.typography.h5.fontWeight,
            fontSize = MaterialTheme.typography.h5.fontSize,
            fontStyle = MaterialTheme.typography.h5.fontStyle,
            textAlign = TextAlign.Center
        )

        IconButton(onClick = {
            coroutineScope.launch {
                try {
                    accountService.getProfile()
                    snackbarController.showDismissibleSnackbar("Refreshed")
                } catch (e: PenguStoreApiException) {
                    snackbarController.showDismissibleSnackbar(e.message)
                }
            }
        }, icon = Icons.Filled.Refresh, description = "Refresh")

    }

    Divider(modifier = Modifier.padding(top = 3.dp, bottom = 15.dp))

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Text(
                text = "User Type:",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.body2.fontSize,
                fontStyle = MaterialTheme.typography.body2.fontStyle,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(
                text = if (store.guest) "Guest" else "Registered",
                fontWeight = MaterialTheme.typography.body2.fontWeight,
                fontSize = MaterialTheme.typography.body2.fontSize,
                fontStyle = MaterialTheme.typography.body2.fontStyle,
                textAlign = TextAlign.Left
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Text(
                text = "Username:",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.body2.fontSize,
                fontStyle = MaterialTheme.typography.body2.fontStyle,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(
                text = store.username,
                fontWeight = MaterialTheme.typography.body2.fontWeight,
                fontSize = MaterialTheme.typography.body2.fontSize,
                fontStyle = MaterialTheme.typography.body2.fontStyle,
                textAlign = TextAlign.Left
            )
        }

        AnimatedVisibility(visible = !store.guest, Modifier.padding(top = 10.dp)) {
            Row {
                Text(
                    text = "Email:",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    fontStyle = MaterialTheme.typography.body2.fontStyle,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = store.email,
                    fontWeight = MaterialTheme.typography.body2.fontWeight,
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    fontStyle = MaterialTheme.typography.body2.fontStyle,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}
