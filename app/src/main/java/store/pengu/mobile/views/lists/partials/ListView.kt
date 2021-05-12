package store.pengu.mobile.views.lists.partials

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.DistanceInfo
import store.pengu.mobile.data.UserList
import store.pengu.mobile.services.ListsService
import store.pengu.mobile.services.MapsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.views.loading.RequestLocation
import store.pengu.mobile.views.partials.IconButton

@ExperimentalAnimationApi
@Composable
fun ListView(
    navController: NavController,
    snackbarController: SnackbarController,
    store: StoreState,
    shareRoute: String,
    listsService: ListsService,
    listId: Long,
    type: UserListType,
    mapsService: MapsService,
    content: @Composable (UserList) -> Unit,
) {
    val list by remember { mutableStateOf(listsService.getList(type, listId)) }
    if (list == null) {
        navController.popBackStack()
        return
    } else {
        store.selectedList = list
    }

    val (loading, setLoading) = remember { mutableStateOf(true) }
    val (location, setLocation) = remember { mutableStateOf(null as LatLng?) }
    val coroutineScope = rememberCoroutineScope()
    var distanceInfo by remember { mutableStateOf(null as DistanceInfo?) }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                list!!.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1.0f, true))
            IconButton(
                onClick = { navController.navigate(shareRoute) },
                icon = Icons.Filled.Share,
                description = "Share list"
            )
        }

        Divider()

        if (loading) {
            RequestLocation(snackbarController, loading, setLoading, setLocation, mapsService)
        } else if (!loading && location != null) {
            coroutineScope.launch {
                try {
                    distanceInfo = mapsService.getDistanceInfo(
                        location,
                        list!!.location
                    )
                } catch (e: Exception) {
                    Log.d("Location", "No location found")
                }
            }
        }

        AnimatedVisibility(visible = distanceInfo != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .alpha(0.7f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = distanceInfo?.duration ?: "",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(end = 3.dp),
                        maxLines = 1
                    )
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = "duration"
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = distanceInfo?.distance ?: "",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.padding(end = 3.dp),
                        maxLines = 1
                    )
                    Icon(
                        imageVector = Icons.Filled.DirectionsCar,
                        contentDescription = "distance"
                    )
                }
            }

            Divider()
        }

        content(list!!)
    }
}