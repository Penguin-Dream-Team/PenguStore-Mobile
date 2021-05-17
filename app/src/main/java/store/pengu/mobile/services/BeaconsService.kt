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
    private var inQueue = false
    private var queueTime = 0

    suspend fun joinQueue(location: LatLng?) {
        location?.let {
            api.joinQueue(location, getNumItems())
            queueTime = Calendar.getInstance().get(Calendar.SECOND)
            inQueue = true
        }
    }

    suspend fun leaveQueue(location: LatLng?) {
        if (inQueue) {
            location?.let {
                val timeInQueue = Calendar.getInstance().get(Calendar.SECOND) - queueTime
                api.leaveQueue(location, getNumItems(), timeInQueue)
                queueTime = 0
            }
            inQueue = false
        }
    }

    private fun getNumItems(): Int {
        var numItems = 0
        store.cartProducts.keys.forEach { list ->
            store.cartProducts[list]?.forEach { product ->
                numItems += product.inCart.value
            }
        }
        return numItems
    }
}