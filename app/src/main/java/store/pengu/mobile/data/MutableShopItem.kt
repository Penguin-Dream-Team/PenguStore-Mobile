package store.pengu.mobile.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import store.pengu.mobile.data.productlists.ProductPantryListEntry

@JsonDeserialize
data class MutableShopItem(
    val productId: Long,
    val listName: String,
    val productName: String,
    val amountNeeded: Int,
    val amountAvailable: Int,
    var inCart: MutableState<Int> = mutableStateOf(0)
)