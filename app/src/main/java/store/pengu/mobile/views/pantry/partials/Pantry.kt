package store.pengu.mobile.views.pantry.partials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import store.pengu.mobile.states.StoreState

@Composable
fun Pantry(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val selectedPantry by remember { mutableStateOf(storeState.selectedPantry) }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            selectedPantry.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "If you want to share this Pantry use this code",
            textAlign = TextAlign.Center
        )

        Text(
            selectedPantry.code,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colors.onSurface)
        ) {
            Text(
                text = "Should be a QRCode to scan the pantry",
                color = MaterialTheme.colors.primary
            )
        }
    }
}