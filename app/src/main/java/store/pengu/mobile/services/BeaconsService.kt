package store.pengu.mobile.services

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.states.StoreState

class BeaconsService(
    private val api: PenguStoreApi,
    private val store: StoreState,
) {
    fun joinQueue() = GlobalScope.launch(Dispatchers.Main) {
        store.location = LatLng(50.11, 50.11)
        store.numItems = 3
        api.joinQueue(store.location!!, store.numItems!!)
    }

    fun leaveQueue() = GlobalScope.launch(Dispatchers.Main) {
        store.location = LatLng(50.11, 50.11)
        store.numItems = 3
        store.timeInQueue = 2
        api.leaveQueue(store.location!!, store.numItems!!, store.timeInQueue!!)
    }

    fun timeQueue() = GlobalScope.launch(Dispatchers.Main) {
        store.location = LatLng(50.11, 50.11)
        api.timeQueue(store.location!!)
    }
}