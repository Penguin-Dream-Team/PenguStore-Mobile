package store.pengu.mobile.api

import com.google.android.gms.maps.model.LatLng
import store.pengu.mobile.api.requests.*
import store.pengu.mobile.api.requests.account.LoginRequest
import store.pengu.mobile.api.requests.account.RegisterRequest
import store.pengu.mobile.api.requests.lists.CartRequest
import store.pengu.mobile.api.requests.lists.CreateListRequest
import store.pengu.mobile.api.requests.products.AddProductToPantryListRequest
import store.pengu.mobile.api.requests.products.AddProductToShoppingListRequest
import store.pengu.mobile.api.responses.account.LoginResponse
import store.pengu.mobile.api.responses.account.ProfileResponse
import store.pengu.mobile.api.responses.account.RegisterResponse
import store.pengu.mobile.api.responses.lists.PantryListResponse
import store.pengu.mobile.api.responses.lists.ShoppingListResponse
import store.pengu.mobile.api.responses.lists.UserListResponse
import store.pengu.mobile.data.*
import store.pengu.mobile.data.productlists.ProductPantryListEntry
import store.pengu.mobile.data.productlists.ProductShoppingListEntry
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
            CreateListRequest(
                newListName,
                newListLocation.latitude,
                newListLocation.longitude,
                newListColor.toString()
            )
        )
    }

    suspend fun createShoppingList(
        newListName: String,
        newListLocation: LatLng,
        newListColor: AvailableListColor
    ): Response.SuccessResponse<ShoppingList> {
        return post(
            Routes.CREATE_SHOPPING_LIST,
            CreateListRequest(
                newListName,
                newListLocation.latitude,
                newListLocation.longitude,
                newListColor.toString()
            )
        )
    }

    suspend fun importPantry(pantryCode: String): Response.SuccessResponse<PantryList> {
        return post(Routes.IMPORT_PANTRY_LIST(pantryCode))
    }

    suspend fun importShoppingList(shoppingListCode: String): Response.SuccessResponse<ShoppingList> {
        return post(Routes.IMPORT_SHOPPING_LIST(shoppingListCode))
    }

    suspend fun getPantryProducts(pantryId: Long): Response.SuccessResponse<List<ProductInPantry>> {
        return get(Routes.GET_PANTRY(pantryId))
    }

    suspend fun getPantryMissingProducts(pantryId: Long): Response.SuccessResponse<List<Product>> {
        return get(Routes.GET_PANTRY_MISSING_PRODUCTS(pantryId))
    }

    suspend fun getShoppingListProducts(shoppingListId: Long): Response.SuccessResponse<List<ProductInShoppingList>> {
        return get(Routes.GET_SHOPPING_LIST(shoppingListId))
    }

    suspend fun getAllProducts(): Response.SuccessResponse<List<Product>> = get(Routes.GET_PRODUCTS)

    suspend fun createProduct(
        name: String,
        barcode: String?,
        image: String?,
    ): Response.SuccessResponse<Product> {
        val request = CreateProductRequest(name, barcode, image)
        return post(Routes.CREATE_PRODUCT, request)
    }

    suspend fun getProduct(productId: Long): Response.SuccessResponse<Product> =
        get(Routes.GET_PRODUCT(productId))

    suspend fun getProductPantryLists(productId: Long): Response.SuccessResponse<List<ProductPantryListEntry>> =
        get(Routes.GET_PRODUCT_PANTRY_LISTS(productId))

    suspend fun getProductShoppingLists(productId: Long): Response.SuccessResponse<List<ProductShoppingListEntry>> =
        get(Routes.GET_PRODUCT_SHOPPING_LISTS(productId))

    suspend fun addProductToPantryList(productId: Long, pantryId: Long, haveAmount: Int, needAmount: Int): Response.SuccessResponse<ProductPantryListEntry> {
        val request = AddProductToPantryListRequest(pantryId, haveAmount, needAmount)
        return post(Routes.ADD_PRODUCT_PANTRY_LIST(productId), request)
    }

    suspend fun addProductToShoppingList(productId: Long, pantryId: Long, price: Double): Response.SuccessResponse<ProductShoppingListEntry> {
        val request = AddProductToShoppingListRequest(pantryId, price)
        return post(Routes.ADD_PRODUCT_SHOPPING_LIST(productId), request)
    }

    suspend fun getProductImages(productId: Long): Response.SuccessResponse<List<String>> =
        get(Routes.GET_PRODUCT_IMAGES(productId))

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

    suspend fun deleteUserPantry(
        userId: Long,
        pantryCode: String
    ): Response.SuccessResponse<String> {
        val deleteUserPantryRequest = DeleteUserPantryRequest(userId, pantryCode)
        return delete(Routes.DELETE_USER_PANTRY, deleteUserPantryRequest)
    }

    suspend fun getUserPantries(userId: Long): Response.SuccessResponse<List<PantryList>> =
        get(Routes.GET_USER_PANTRIES, userId.toString())

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

    suspend fun getUserShoppingList(shopId: Long): Response.SuccessResponse<List<ProductInShoppingList>> =
        get(Routes.GET_USER_SHOPPING_LIST, shopId.toString())

    suspend fun updateSmartSortingEntries(
        productId: Long,
        remainingItems: List<Long>
    ): Response.SuccessResponse<Boolean> =
        put(Routes.UPDATE_SMART_SORTING_ENTRIES.replace("id", productId.toString()), remainingItems)

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

    suspend fun addBarcodeProduct(barcode: String): Response.SuccessResponse<String> =
        put(Routes.ADD_BARCODE_PRODUCT, barcode)

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

    suspend fun buyCart(cartRequest: CartRequest): Response.SuccessResponse<String> =
        post(Routes.BUY_CART, cartRequest)

    suspend fun getProductSuggestion(barcode: String): Response.SuccessResponse<Product> =
        get(Routes.GET_PRODUCT_SUGGESTION(barcode))

    suspend fun joinQueue(location: LatLng, numItems: Int): Response.SuccessResponse<String> =
        post(Routes.JOIN_QUEUE, location, numItems)

    suspend fun leaveQueue(
        location: LatLng,
        numItems: Int,
        time: Int
    ): Response.SuccessResponse<String> {
        return post(
            Routes.LEAVE_QUEUE,
            LeaveQueueRequest(location.latitude, location.longitude, numItems, time)
        )
    }

    suspend fun timeQueue(location: LatLng): Response.SuccessResponse<Int> =
        get(Routes.TIME_QUEUE, location)

    suspend fun addProductImage(
        id: Int,
        barcode: String?,
        productId: Long?,
        imageUrl: String
    ): Response.SuccessResponse<String> {
        return post(
            Routes.ADD_IMAGE,
            ImageRequest(id.toULong(), barcode, productId, imageUrl)
        )
    }

    suspend fun deleteProductImage(
        id: Int,
        barcode: String?,
        productId: Long?,
        imageUrl: String
    ): Response.SuccessResponse<String> {
        return delete(
            Routes.DELETE_IMAGE,
            ImageRequest(id.toULong(), barcode, productId, imageUrl)
        )
    }


    suspend fun translation(string: String): Response.SuccessResponse<String> =
        get(Routes.TRANSLATION, string)

    suspend fun rateProduct(barcode: String, rating: Int): Response.SuccessResponse<String> {
        val route = Routes.RATE_PRODUCT.replace("rating", rating.toString())
        return post(route, barcode)
    }
}