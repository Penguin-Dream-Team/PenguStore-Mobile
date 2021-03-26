package store.pengu.mobile.api

object Routes {
    const val DASHBOARD = "/dashboard"
    const val LOGIN = "/login"
    const val SETUP = "/setup"

    const val USERS = "/users"
    const val GET_USER = "/users/id"
    const val ADD_USER = "/users/add"
    const val UPDATE_USER = "/users/update"
    const val ADD_USER_PANTRY = "/users/addPantry"
    const val DELETE_USER_PANTRY = "/users/deletePantry"
    const val GET_USER_PANTRIES = "/users/id/pantries"
    const val GET_USER_SHOPPING_LIST = "/users/id/shoppingList"

    const val PANTRIES = "/pantries"
    const val GET_PANTRY = "/pantries/id"
    const val ADD_PANTRY = "/pantries/add"
    const val UPDATE_PANTRY= "/pantries/update"
    const val ADD_PANTRY_PRODUCT = "/pantries/addProduct"
    const val UPDATE_PANTRY_PRODUCT = "/pantries/updateProduct"
    const val DELETE_PANTRY_PRODUCT = "/pantries/deleteProduct"
    const val GET_PANTRY_PRODUCTS = "/pantries/id/products"

    const val PRODUCTS = "/products"
    const val GET_PRODUCT = "/products/id"
    const val ADD_PRODUCT = "/products/add"
    const val UPDATE_PRODUCT = "/products/update"

    const val SHOPS = "/shops"
    const val GET_SHOP = "/shops/id"
    const val ADD_SHOP = "/shops/add"
    const val UPDATE_SHOP = "/shops/update"
    const val ADD_SHOP_PRODUCT = "/shops/addProduct"
    const val UPDATE_SHOP_PRODUCT = "/shops/updateProduct"
    const val DELETE_SHOP_PRODUCT = "/shops/deleteProduct"
    const val GET_SHOP_PRODUCTS = "/shops/id/products"
}
