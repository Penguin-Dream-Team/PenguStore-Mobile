package store.pengu.mobile.views.pantry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.states.StoreState

@Composable
fun PantryScreen(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val openDialog = remember { mutableStateOf(false) }
    var selectedPantry by remember { mutableStateOf(storeState.selectedPantry) }

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(store.pantries) { pantry ->
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
                            Text(
                                text = pantry.name,
                                fontWeight = FontWeight.SemiBold
                            )

                            IconButton(
                                onClick = {
                                    selectedPantry = pantry
                                    openDialog.value = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Preview,
                                    tint = Color(52, 247, 133),
                                    contentDescription = "Preview"
                                )
                            }

                            IconButton(
                                onClick = {
                                    storeState.selectedPantry = pantry
                                    navController.navigate("pantry")
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.More,
                                    tint = Color(52, 247, 133),
                                    contentDescription = "See More"
                                )
                            }
                        }
                    }
                }
            }
        }

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

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = selectedPantry.name)
                },
                text = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        /*items(selectedPantry.value.items) { item ->
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
                                        Text(
                                            text = "${item.name}: ${item.amountAvailable} out of ${item.amountNeeded}",
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }*/
                    }
                },
                confirmButton = {},
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}