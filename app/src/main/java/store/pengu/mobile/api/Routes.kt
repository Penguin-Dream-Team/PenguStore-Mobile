package store.pengu.mobile.api

import store.pengu.mobile.errors.PenguStoreApiException

object Routes {
    const val LOGIN = "/login"
    const val REFRESH_TOKEN = "/login/refresh"
    const val REGISTER_GUEST = "/register/guest"
    const val USER_PROFILE = "/profile"

    /**
     * Update the user's profile
     *
     * @throws PenguStoreApiException
     */
    const val UPDATE_USER = "/users/update"

    /**
     * Find list in specified location
     *
     * @throws PenguStoreApiException if no list found
     */
    const val FIND_LIST = "/lists/find"


    const val DASHBOARD = "/dashboard"

    const val SETUP = "/setup"

    const val USERS = "/users"
    const val GET_USER = "/users/id"
    const val ADD_USER = "/users/add"
    const val ADD_USER_PANTRY = "/users/addPantry"
    const val DELETE_USER_PANTRY = "/users/deletePantry"
    const val GET_USER_PANTRIES = "/users/id/pantries"
    const val GET_USER_SHOPPING_LIST_PRODUCTS = "/users/id/shoppingList"
    const val ADD_SHOPPING_LIST = "/users/addShoppingList"
    const val UPDATE_SHOPPING_LIST = "/users/updateShoppingList"
    const val DELETE_SHOPPING_LIST = "/users/deleteShoppingList"
    const val GET_USER_SHOPPING_LISTS = "/users/id/ShoppingLists"
    const val GET_USER_SHOPPING_LIST = "/users/id/ShoppingList/"

    const val PANTRIES = "/pantries"
    const val GET_PANTRY_LIST = "/pantries/id"
    const val ADD_PANTRY_LIST = "/pantries/add"
    const val UPDATE_PANTRY_LIST= "/pantries/update"
    const val ADD_PANTRY_PRODUCT = "/pantries/addProduct"
    const val UPDATE_PANTRY_PRODUCT = "/pantries/updateProduct"
    const val DELETE_PANTRY_PRODUCT = "/pantries/deleteProduct"
    const val GET_PANTRY_PRODUCTS = "/pantries/id/products"

    const val PRODUCTS = "/products"
    const val GET_PRODUCT = "/products/id"
    const val ADD_PRODUCT = "/products/add"
    const val UPDATE_PRODUCT = "/products/update"
    const val ADD_BARCODE_PRODUCT = "/products/addBarcode"

    const val SHOPS = "/shops"
    const val GET_SHOP = "/shops/id"
    const val ADD_SHOP = "/shops/add"
    const val UPDATE_SHOP = "/shops/update"
    const val ADD_SHOP_PRODUCT = "/shops/addProduct"
    const val UPDATE_SHOP_PRODUCT = "/shops/updateProduct"
    const val DELETE_SHOP_PRODUCT = "/shops/deleteProduct"
    const val GET_SHOP_PRODUCTS = "/shops/id/products"
}
