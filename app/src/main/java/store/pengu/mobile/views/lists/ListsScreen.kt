package store.pengu.mobile.views.lists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
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
fun ListsScreen(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }
    val openDialog = remember { mutableStateOf(false) }

    var type by remember { mutableStateOf(0) }
    val selectedTabList by remember {
        mutableStateOf(storeState.lists)
    }
    val sectionTypes = ListTypesEnum.values().map { it.type }

    Column {
        TabRow(selectedTabIndex = type) {
            sectionTypes.forEachIndexed { index, content ->
                Tab(
                    modifier = Modifier.padding(bottom = 10.dp),
                    selected = type == index,
                    content = { Text(content) },
                    onClick = { type = index }
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 20.dp)
        ) {
            items(selectedTabList[type]) { item ->
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
                                text = item.name,
                                fontWeight = FontWeight.SemiBold
                            )

                            IconButton(
                                onClick = {
                                    storeState.selectedList = item
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
                                    storeState.selectedList = item
                                    if (type == 0)
                                        navController.navigate("pantry_list")
                                    else
                                        navController.navigate("shopping_list")
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
    }

    Button(
        onClick = {
            store.listType = type
            navController.navigate("new_list")
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val text = if (type == 0) "Create new Pantry List" else "Create new Shopping List"
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = storeState.selectedList.name)
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

private enum class ListTypesEnum(val type: String) {
    PantryList("PantryList"),
    ShoppingList("Shopping List")
}

