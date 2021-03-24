package store.pengu.mobile.views.pantry.partials

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import store.pengu.mobile.states.StoreState

@Composable
fun NewPantry(navController: NavController, store: StoreState) {
    var text by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Pantry Name") }
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(store.items) { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = {
                                    item.amountNeeded--
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Remove,
                                    contentDescription = "Remove",
                                    tint = Color(52, 247, 133)
                                )
                            }
                            Text(
                                text = "${item.name}: ${item.amountNeeded}",
                                fontWeight = FontWeight.SemiBold
                            )
                            IconButton(
                                onClick = {
                                    item.amountNeeded++
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    tint = Color(52, 247, 133),
                                    contentDescription = "Add"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}