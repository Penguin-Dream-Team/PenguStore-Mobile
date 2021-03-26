package store.pengu.mobile.api

import store.pengu.mobile.api.requests.*
import store.pengu.mobile.data.*
import store.pengu.mobile.states.StoreState

class PenguStoreApi(
    store: StoreState
) : ApiHandler(store) {

    suspend fun login(loginRequest: LoginRequest): Response.SuccessResponse<User> = post(Routes.LOGIN, loginRequest)

    suspend fun setup(): Response.SuccessResponse<User> {
        val setupRequest = SetupRequest(phonePublicKey = "DUMMY")
        return post(Routes.SETUP, setupRequest)
    }

    suspend fun dashboard(): Response.SuccessResponse<String> = get(Routes.DASHBOARD)

    suspend fun users(): Response.SuccessResponse<String> = get(Routes.USERS)

    suspend fun getUser(userId: String): Response.SuccessResponse<User> = get(Routes.GET_USER, userId)

    suspend fun addUser(username: String, email: String, password: String
    ): Response.SuccessResponse<String> {
        val addUserRequest = AddUserRequest(username, email, password)
        return post(Routes.ADD_USER, addUserRequest)
    }

    suspend fun updateUser(userId: Long, username: String, email: String, password: String
    ): Response.SuccessResponse<String> {
        val updateUserRequest = UpdateUserRequest(userId, username, email, password)
        return put(Routes.UPDATE_USER, updateUserRequest)
    }

    suspend fun addUserPantry(userId: Long, pantryCode: String): Response.SuccessResponse<String> {
        val addUserPantryRequest = AddUserPantryRequest(userId, pantryCode)
        return post(Routes.ADD_USER_PANTRY, addUserPantryRequest)
    }

    suspend fun deleteUserPantry(userId: Long, pantryCode: String): Response.SuccessResponse<String> {
        val addUserPantryRequest = DeleteUserPantryRequest(userId, pantryCode)
        return delete(Routes.ADD_USER_PANTRY, addUserPantryRequest)
    }

    suspend fun getUserPantries(userId: String): Response.SuccessResponse<List<Pantry>> = get(Routes.GET_USER_PANTRIES, userId)

    suspend fun getUserShoppingList(shopId: String): Response.SuccessResponse<List<ShoppingList>> = get(Routes.GET_USER_SHOPPING_LIST, shopId)

    suspend fun pantries(): Response.SuccessResponse<List<Pantry>> = get(Routes.PANTRIES)

    suspend fun getPantry(pantryId: String): Response.SuccessResponse<Pantry> = get(Routes.GET_PANTRY, pantryId)

    suspend fun addPantry(pantryId: Long, pantryCode: String, pantryName: String): Response.SuccessResponse<String> {
        val addPantryRequest = AddPantryRequest(pantryId, pantryCode, pantryName)
        return post(Routes.ADD_PANTRY, addPantryRequest)
    }

    suspend fun updatePantry(pantryId: Long, pantryCode: String, pantryName: String): Response.SuccessResponse<String> {
        val updatePantryRequest = UpdatePantryRequest(pantryId, pantryCode, pantryName)
        return put(Routes.UPDATE_PANTRY, updatePantryRequest)
    }

    suspend fun addPantryProduct(pantryId: Long, productId: Long, amountAvailable: Int, amountNeeded: Int): Response.SuccessResponse<String> {
        val addPantryProductRequest = AddPantryProductRequest(pantryId, productId, amountAvailable, amountNeeded)
        return post(Routes.ADD_PANTRY_PRODUCT, addPantryProductRequest)
    }

    suspend fun updatePantryProduct(pantryId: Long, productId: Long, amountAvailable: Int, amountNeeded: Int): Response.SuccessResponse<String> {
        val updatePantryProductRequest = UpdatePantryProductRequest(pantryId, productId, amountAvailable, amountNeeded)
        return put(Routes.UPDATE_PANTRY_PRODUCT, updatePantryProductRequest)
    }

    suspend fun deletePantryProduct(pantryId: Long, productId: Long): Response.SuccessResponse<String> {
        val deletePantryProductRequest = DeletePantryProductRequest(pantryId, productId, -1, -1)
        return delete(Routes.DELETE_PANTRY_PRODUCT, deletePantryProductRequest)
    }

    suspend fun getPantryProducts(pantryId: String): Response.SuccessResponse<List<Product>> = get(Routes.GET_PANTRY_PRODUCTS, pantryId)

    suspend fun products(): Response.SuccessResponse<List<Product>> = get(Routes.PRODUCTS)

    suspend fun getProduct(productId: String): Response.SuccessResponse<Product> = get(Routes.GET_PRODUCT, productId)

    suspend fun addProduct(productId: Long, productName: String, productBarCode: String, reviewScore: Double, reviewNumber: Int): Response.SuccessResponse<String> {
        val addProductRequest = AddProductRequest(productId, productName, productBarCode, reviewScore, reviewNumber)
        return post(Routes.ADD_PRODUCT, addProductRequest)
    }

    suspend fun updateProduct(productId: Long, productName: String, productBarCode: String, reviewScore: Double, reviewNumber: Int): Response.SuccessResponse<String> {
        val updateProductRequest = UpdateProductRequest(productId, productName, productBarCode, reviewScore, reviewNumber)
        return put(Routes.UPDATE_PRODUCT, updateProductRequest)
    }

    suspend fun shops(): Response.SuccessResponse<List<Shop>> = get(Routes.SHOPS)

    suspend fun getShop(shopId: String): Response.SuccessResponse<Shop> = get(Routes.GET_SHOP, shopId)

    suspend fun addShop(shopId: Long, shopName: String, shopLocationX: Float, shopLocationY: Float): Response.SuccessResponse<String> {
        val addShopRequest = AddShopRequest(shopId, shopName, shopLocationX, shopLocationY)
        return post(Routes.ADD_SHOP, addShopRequest)
    }

    suspend fun updateShop(shopId: Long, shopName: String, shopLocationX: Float, shopLocationY: Float): Response.SuccessResponse<String> {
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

    suspend fun getShopProducts(shopId: String): Response.SuccessResponse<List<Product>> = get(Routes.GET_SHOP_PRODUCTS, shopId)
}