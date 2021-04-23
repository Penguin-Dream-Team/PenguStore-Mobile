package store.pengu.mobile.api

import com.google.android.gms.maps.model.LatLng
import store.pengu.mobile.api.requests.*
import store.pengu.mobile.api.requests.account.LoginRequest
import store.pengu.mobile.api.requests.account.RefreshTokenRequest
import store.pengu.mobile.api.requests.account.RegisterRequest
import store.pengu.mobile.api.requests.lists.CreateListRequest
import store.pengu.mobile.api.responses.account.LoginResponse
import store.pengu.mobile.api.responses.account.ProfileResponse
import store.pengu.mobile.api.responses.account.RegisterResponse
import store.pengu.mobile.api.responses.lists.PantryListResponse
import store.pengu.mobile.api.responses.lists.ShoppingListResponse
import store.pengu.mobile.api.responses.lists.UserListResponse
import store.pengu.mobile.data.*
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.views.lists.AvailableListColor

class PenguStoreApi(
    store: StoreState
) : ApiHandler(store) {

    suspend fun registerGuest(username: String): RegisterResponse {
        val registerRequest = RegisterRequest(username)
        return post(Routes.REGISTER_GUEST, registerRequest)
    }

    suspend fun login(username: String, password: String): LoginResponse {
        val loginRequest = LoginRequest(username, password)
        return post(Routes.LOGIN, loginRequest)
    }

    suspend fun refreshToken(refreshToken: String): LoginResponse {
        val refreshRequest = RefreshTokenRequest(refreshToken)
        return post(Routes.REFRESH_TOKEN, refreshRequest)
    }

    suspend fun getProfile(): ProfileResponse {
        return get(Routes.USER_PROFILE)
    }

    suspend fun updateAccount(
        username: String? = null,
        email: String? = null,
        password: String? = null
    ): ProfileResponse {
        val updateUserRequest = UpdateUserRequest(username, email, password)
        return put(Routes.UPDATE_USER, updateUserRequest)
    }

    suspend fun findList(
        latitude: Double,
        longitude: Double
    ): UserListResponse {
        return get(Routes.FIND_LIST, mapOf("latitude" to latitude, "longitude" to longitude))
    }

    suspend fun getPantryLists(): PantryListResponse {
        return get(Routes.GET_PANTRIES)
    }

    suspend fun getShoppingLists(): ShoppingListResponse {
        return get(Routes.GET_SHOPPING_LISTS)
    }

    suspend fun createPantryList(
        newListName: String,
        newListLocation: LatLng,
        newListColor: AvailableListColor
    ): Response.SuccessResponse<PantryList> {
        return post(
            Routes.CREATE_PANTRY_LIST,
            CreateListRequest(newListName, newListLocation.latitude, newListLocation.longitude, newListColor.toString())
        )
    }

    suspend fun createShoppingList(
        newListName: String,
        newListLocation: LatLng,
        newListColor: AvailableListColor
    ): Response.SuccessResponse<ShoppingList> {
        return post(
            Routes.CREATE_SHOPPING_LIST,
            CreateListRequest(newListName, newListLocation.latitude, newListLocation.longitude, newListColor.toString())
        )
    }

    /**
     * NEEDS REWRITE
     */

    suspend fun dashboard(): Response.SuccessResponse<String> = get(Routes.DASHBOARD)

    suspend fun users(): Response.SuccessResponse<String> = get(Routes.USERS)

    suspend fun getUser(userId: String): Response.SuccessResponse<User> =
        get(Routes.GET_USER, userId)

    suspend fun addUser(
        username: String, email: String, password: String
    ): Response.SuccessResponse<String> {
        val addUserRequest = AddUserRequest(username, email, password)
        return post(Routes.ADD_USER, addUserRequest)
    }

    suspend fun addUserPantry(userId: Long, pantryCode: String): Response.SuccessResponse<String> {
        val addUserPantryRequest = AddUserPantryRequest(userId, pantryCode)
        return post(Routes.ADD_USER_PANTRY, addUserPantryRequest)
    }

    suspend fun deleteUserPantry(
        userId: Long,
        pantryCode: String
    ): Response.SuccessResponse<String> {
        val deleteUserPantryRequest = DeleteUserPantryRequest(userId, pantryCode)
        return delete(Routes.DELETE_USER_PANTRY, deleteUserPantryRequest)
    }

    suspend fun getUserPantries(userId: Long): Response.SuccessResponse<List<PantryList>> =
        get(Routes.GET_USER_PANTRIES, userId.toString())

    suspend fun getUserShoppingListProducts(shopId: Long): Response.SuccessResponse<List<Product>> =
        get(Routes.GET_USER_SHOPPING_LIST_PRODUCTS, shopId.toString())

    suspend fun addShoppingList(
        shopId: Long,
        userId: Long,
        name: String
    ): Response.SuccessResponse<String> {
        val addShoppingListRequest = AddShoppingListRequest(shopId, userId, name)
        return post(Routes.ADD_SHOPPING_LIST, addShoppingListRequest)
    }

    suspend fun updateShoppingList(
        shopId: Long,
        userId: Long,
        name: String
    ): Response.SuccessResponse<String> {
        val updateShoppingListRequest = UpdateShoppingListRequest(shopId, userId, name)
        return post(Routes.UPDATE_SHOPPING_LIST, updateShoppingListRequest)
    }

    suspend fun deleteShoppingList(
        shopId: Long,
        userId: Long,
        name: String
    ): Response.SuccessResponse<String> {
        val deleteShoppingListRequest = DeleteShoppingListRequest(shopId, userId, name)
        return post(Routes.DELETE_SHOPPING_LIST, deleteShoppingListRequest)
    }

    suspend fun getUserShoppingList(
        userId: Long,
        shopId: String
    ): Response.SuccessResponse<ShoppingList2> =
        get(Routes.GET_USER_SHOPPING_LIST + shopId, userId.toString())

    suspend fun pantries(): Response.SuccessResponse<List<PantryList>> = get(Routes.GET_PANTRIES)

    suspend fun getPantry(pantryId: String): Response.SuccessResponse<PantryList> =
        get(Routes.GET_PANTRY_LIST, pantryId)

    suspend fun addPantry(
        pantryName: String,
        pantryLocation: LatLng
    ): Response.SuccessResponse<PantryList> {
        val addPantryRequest = AddPantryListRequest(
            name = pantryName,
            latitude = pantryLocation.latitude,
            longitude = pantryLocation.longitude
        )
        return post(Routes.ADD_PANTRY_LIST, addPantryRequest)
    }

    suspend fun updatePantry(
        pantryName: String,
        pantryLocation: LatLng
    ): Response.SuccessResponse<String> {
        val updatePantryRequest = UpdatePantryListRequest(
            name = pantryName,
            latitude = pantryLocation.latitude,
            longitude = pantryLocation.longitude
        )
        return put(Routes.UPDATE_PANTRY_LIST, updatePantryRequest)
    }

    suspend fun addPantryProduct(
        pantryId: Long,
        productId: Long,
        amountAvailable: Int,
        amountNeeded: Int
    ): Response.SuccessResponse<String> {
        val addPantryProductRequest =
            AddPantryProductRequest(pantryId, productId, amountAvailable, amountNeeded)
        return post(Routes.ADD_PANTRY_PRODUCT, addPantryProductRequest)
    }

    suspend fun updatePantryProduct(
        pantryId: Long,
        productId: Long,
        amountAvailable: Int,
        amountNeeded: Int
    ): Response.SuccessResponse<String> {
        val updatePantryProductRequest =
            UpdatePantryProductRequest(pantryId, productId, amountAvailable, amountNeeded)
        return put(Routes.UPDATE_PANTRY_PRODUCT, updatePantryProductRequest)
    }

    suspend fun deletePantryProduct(
        pantryId: Long,
        productId: Long
    ): Response.SuccessResponse<String> {
        val deletePantryProductRequest = DeletePantryProductRequest(pantryId, productId, -1, -1)
        return delete(Routes.DELETE_PANTRY_PRODUCT, deletePantryProductRequest)
    }

    suspend fun getPantryProducts(pantryId: Long): Response.SuccessResponse<List<Product>> =
        get(Routes.GET_PANTRY_PRODUCTS, pantryId.toString())

    suspend fun products(): Response.SuccessResponse<List<Product>> = get(Routes.PRODUCTS)

    suspend fun getProduct(productId: String): Response.SuccessResponse<Product> =
        get(Routes.GET_PRODUCT, productId)

    suspend fun addProduct(
        productId: Long,
        productName: String,
        productBarCode: String,
        reviewScore: Double,
        reviewNumber: Int
    ): Response.SuccessResponse<String> {
        val addProductRequest =
            AddProductRequest(productId, productName, productBarCode, reviewScore, reviewNumber)
        return post(Routes.ADD_PRODUCT, addProductRequest)
    }

    suspend fun updateProduct(
        productId: Long,
        productName: String,
        productBarCode: String,
        reviewScore: Double,
        reviewNumber: Int
    ): Response.SuccessResponse<String> {
        val updateProductRequest =
            UpdateProductRequest(productId, productName, productBarCode, reviewScore, reviewNumber)
        return put(Routes.UPDATE_PRODUCT, updateProductRequest)
    }

    suspend fun addBarcodeProduct(barcode: String): Response.SuccessResponse<String> = put(Routes.ADD_BARCODE_PRODUCT, barcode)

    suspend fun shops(): Response.SuccessResponse<List<ShoppingList>> = get(Routes.SHOPS)

    suspend fun getShop(shopId: String): Response.SuccessResponse<ShoppingList> =
        get(Routes.GET_SHOP, shopId)

    suspend fun addShop(
        shopId: Long,
        shopName: String,
        shopLocationX: Float,
        shopLocationY: Float
    ): Response.SuccessResponse<String> {
        val addShopRequest = AddShopRequest(shopId, shopName, shopLocationX, shopLocationY)
        return post(Routes.ADD_SHOP, addShopRequest)
    }

    suspend fun updateShop(
        shopId: Long,
        shopName: String,
        shopLocationX: Float,
        shopLocationY: Float
    ): Response.SuccessResponse<String> {
        val updateShopRequest = UpdateShopRequest(shopId, shopName, shopLocationX, shopLocationY)
        return put(Routes.UPDATE_SHOP, updateShopRequest)
    }

    suspend fun addShopProduct(): Response.SuccessResponse<String> {
        val addShopProductRequest = AddShopProductRequest()
        return post(Routes.ADD_SHOP_PRODUCT, addShopProductRequest)
    }

    suspend fun updateShopProduct(): Response.SuccessResponse<String> {
        val updateShopProductRequest = UpdateShopProductRequest()
        return put(Routes.UPDATE_SHOP_PRODUCT, updateShopProductRequest)
    }

    suspend fun deleteShopProduct(): Response.SuccessResponse<String> {
        val deleteShopProductRequest = DeleteShopProductRequest()
        return delete(Routes.DELETE_SHOP_PRODUCT, deleteShopProductRequest)
    }

    suspend fun getShopProducts(shopId: String): Response.SuccessResponse<List<Product>> =
        get(Routes.GET_SHOP_PRODUCTS, shopId)

    suspend fun joinQueue(location: LatLng, numItems: Int): Response.SuccessResponse<String> =
        post(Routes.JOIN_QUEUE, location, numItems)

    suspend fun leaveQueue(location: LatLng, numItems: Int, time: Int): Response.SuccessResponse<String> {
        return post(
            Routes.LEAVE_QUEUE,
            LeaveQueueRequest(location.latitude, location.longitude, numItems, time)
        )
    }

    suspend fun timeQueue(location: LatLng): Response.SuccessResponse<String> =
        get(Routes.TIME_QUEUE, location)
}