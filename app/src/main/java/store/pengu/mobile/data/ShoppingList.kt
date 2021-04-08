package store.pengu.mobile.data

import com.google.gson.annotations.SerializedName

data class ShoppingList (
    @SerializedName("shopId") val shopId: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("name") override val name: String
) : ListTypes