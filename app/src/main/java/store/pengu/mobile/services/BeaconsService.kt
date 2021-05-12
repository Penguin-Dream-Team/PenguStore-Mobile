package store.pengu.mobile.services

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.states.StoreState
import java.util.*

class BeaconsService(
    private val api: PenguStoreApi,
    private val store: StoreState,
) {
    fun joinQueue() = GlobalScope.launch(Dispatchers.Main) {
        store.location = LatLng(50.25, 150.25)
        api.joinQueue(store.location!!, store.numItems!!)
        store.joinQueueTime = Calendar.getInstance().get(Calendar.SECOND)
    }

    fun leaveQueue() = GlobalScope.launch(Dispatchers.Main) {
        store.location = LatLng(50.25, 150.25)
        val timeInQueue = Calendar.getInstance().get(Calendar.SECOND) - store.joinQueueTime!!
        api.leaveQueue(store.location!!, store.numItems!!, timeInQueue)
    }
}