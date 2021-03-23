package store.pengu.mobile.views.pantry.partials

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import store.pengu.mobile.data.Item
import store.pengu.mobile.states.StoreState

@Composable
fun NewPantry(navController: NavController, store: StoreState) {
    var text by remember { mutableStateOf("") }
    val items: List<Item> by mutableStateOf(store.items)

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Pantry Name") }
        )

        LazyColumnFor(
            items,
            modifier = Modifier.fillMaxWidth()
        ) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                item.amountNeeded--
                            },
                        ) {
                            Icon(
                                asset = Icons.Filled.ThumbUp,
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
                            },
                        ) {
                            Icon(
                                asset = Icons.Filled.Add,
                                tint = Color(52, 247, 133)
                            )
                        }
                    }
                }

                /*Row {
                    IconButton(
                        onClick = {
                            if (!loading.value) {
                                GlobalScope.launch {
                                    loading.value = true
                                    val handler = Handler(Looper.getMainLooper())
                                    try {
                                        store.client?.approvePendingTransactions(
                                            transaction.id,
                                            transaction.token
                                        )
                                        handler.post {
                                            Toasts.notifyUser(context, "Approved transaction")
                                        }
                                        store.client?.refreshPendingTransactions()
                                    } catch (e: Exception) {
                                        handler.post {
                                            Toasts.notifyUser(context, e.message!!)
                                        }
                                    }
                                    loading.value = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            asset = Icons.Filled.CheckCircle,
                            tint = Color(52, 247, 133)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (!loading.value) {
                                GlobalScope.launch {
                                    loading.value = true
                                    val handler = Handler(Looper.getMainLooper())
                                    try {
                                        store.client?.cancelPendingTransactions(
                                            transaction.id,
                                            transaction.token
                                        )
                                        handler.post {
                                            Toasts.notifyUser(context, "Canceled transaction")
                                        }
                                        store.client?.refreshPendingTransactions()
                                    } catch (e: Exception) {
                                        handler.post {
                                            Toasts.notifyUser(context, e.message!!)
                                        }
                                    }
                                    loading.value = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            asset = Icons.Filled.Cancel,
                            tint = Color(247, 72, 52)
                        )
                    }
                }*/
            }
        }
    }
}