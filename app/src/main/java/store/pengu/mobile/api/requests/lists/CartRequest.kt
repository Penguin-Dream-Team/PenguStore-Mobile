package store.pengu.mobile.api.requests.lists

import store.pengu.mobile.data.CartProduct

data class CartRequest(
    val cartItems: List<CartProduct>
)
