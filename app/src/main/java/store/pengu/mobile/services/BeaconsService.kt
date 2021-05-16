package store.pengu.mobile.services

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
        api.joinQueue(store.location!!, getNumItems())
        store.joinQueueTime = Calendar.getInstance().get(Calendar.SECOND)
    }

    fun leaveQueue() = GlobalScope.launch(Dispatchers.Main) {
        val timeInQueue = Calendar.getInstance().get(Calendar.SECOND) - store.joinQueueTime!!
        api.leaveQueue(store.location!!, getNumItems(), timeInQueue)
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