package store.pengu.mobile.views.dashboard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.dashboard.partials.SetupScreen
import store.pengu.mobile.views.dashboard.partials.UserInfo
import store.pengu.mobile.views.pantry.PantryScreen
import store.pengu.mobile.views.partials.ItemCard

@ExperimentalAnimationApi
@Composable
fun DashboardScreen(navController: NavController, store: StoreState) {
    val storeState by remember { mutableStateOf(store) }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(top = 10.dp, bottom = 20.dp)
        ) {
            ItemCard(
                name = "Pug",
                tagLine = "This is a dog",
                image = "https://files.perpheads.com/TxGQwRWwBhg870HC.png",
                modifier = Modifier.padding(end = 15.dp)
            )
            ItemCard(
                name = "Fish",
                image = "https://files.perpheads.com/4ZWdaILTQqHmR3xB.png",
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            ItemCard(
                name = "Penguin",
                tagLine = "This is a penguin",
                image = "https://files.perpheads.com/oaSmfYWFExWAqCVe.jpg",
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            ItemCard(
                name = "Horse",
                tagLine = "This is a horse",
                image = "https://files.perpheads.com/OhD6MSeDHHSmSAuN.png",
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            ItemCard(
                name = "Cat",
                tagLine = "This is a cat",
                image = "https://files.perpheads.com/UBQl0mjTOwFj3tA9.png",
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            ItemCard(
                name = "Cat",
                tagLine = "This is a cat",
                image = "https://files.perpheads.com/qdF68OawxJOSHbzk.png",
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        if (storeState.userType == "") {
            SetupScreen(navController, storeState)
        } else {
            UserInfo(storeState)

            PantryScreen(navController, storeState)
        }
    }
}
