@file:Suppress("FunctionName")

package store.pengu.mobile.api

import store.pengu.mobile.errors.PenguStoreApiException

object Routes {
    const val LOGIN = "/login"
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

    const val GET_PANTRIES = "/lists/pantries"
    const val GET_SHOPPING_LISTS = "/lists/shops"

    const val CREATE_PANTRY_LIST = "/lists/pantries"
    const val CREATE_SHOPPING_LIST = "/lists/shops"

    fun IMPORT_PANTRY_LIST(code: String) = "/lists/pantries/import/$code"
    fun IMPORT_SHOPPING_LIST(code: String) = "/lists/shops/import/$code"
    fun GET_PANTRY(pantryId: Long) = "/lists/pantries/$pantryId"
    fun GET_SHOPPING_LIST(shoppingListId: Long) = "/lists/shops/$shoppingListId"
    fun GET_PANTRY_MISSING_PRODUCTS(pantryId: Long) = "/lists/pantries/$pantryId/missing"

    const val GET_PRODUCTS = "/products"
    const val CREATE_PRODUCT = "/products"
    fun GET_PRODUCT(productId: Long) = "/products/$productId"
    fun GET_PRODUCT_PANTRY_LISTS(productId: Long) = "/products/$productId/pantries"
    fun GET_PRODUCT_SHOPPING_LISTS(productId: Long) = "/products/$productId/shops"
    fun ADD_PRODUCT_PANTRY_LIST(productId: Long) = "/products/$productId/pantries"
    fun ADD_PRODUCT_SHOPPING_LIST(productId: Long) = "/products/$productId/shops"
    fun GET_PRODUCT_IMAGES(productId: Long) = "/products/$productId/images"
    fun RATE_PRODUCT(productId: Long, rating: Int) = "/products/$productId/rate/$rating"
    fun EDIT_PRODUCT(productId: Long) = "/products/$productId"

    const val BUY_CART = "/cart"

    fun GET_PRODUCT_SUGGESTION(barcode: String) = "/cart/suggestion/$barcode"

    fun UPDATE_SMART_SORTING_ENTRIES(shoppingListId: Long, barcode: String) = "/shoppingList/$shoppingListId/smartSortingInfo/$barcode"

    const val TIME_QUEUE = "/queue/time"

    /**
     * NEED REWRITE
     */

    const val DASHBOARD = "/dashboard"

    const val USERS = "/users"
    const val GET_USER = "/users/id"
    const val ADD_USER = "/users/add"
    const val DELETE_USER_PANTRY = "/users/deletePantry"
    const val GET_USER_PANTRIES = "/users/id/pantries"
    const val UPDATE_SHOPPING_LIST = "/users/updateShoppingList"
    const val DELETE_SHOPPING_LIST = "/users/deleteShoppingList"
    const val GET_USER_SHOPPING_LIST = "/shoppingList/id"

    const val GET_PANTRY_LIST = "/pantries/id"
    const val ADD_PANTRY_LIST = "/pantries/add"
    const val UPDATE_PANTRY_LIST= "/pantries/update"
    const val ADD_PANTRY_PRODUCT = "/pantries/addProduct"
    const val UPDATE_PANTRY_PRODUCT = "/pantries/updateProduct"
    const val DELETE_PANTRY_PRODUCT = "/pantries/deleteProduct"

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

    const val JOIN_QUEUE = "/queue/join/"
    const val LEAVE_QUEUE = "/queue/leave"

    const val ADD_IMAGE = "/images/addImage"
    const val DELETE_IMAGE = "/images/deleteImage"

    const val TRANSLATION = "/translation/id"
}
