package store.pengu.mobile.services

import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.storage.UserDataService
import store.pengu.mobile.storage.proto.UserData
import java.util.*

class AccountService(
    private val userDataService: UserDataService,
    private val api: PenguStoreApi,
    private val store: StoreState
) {

    suspend fun hasLoggedInBefore(): Boolean {
        return userDataService.getUserData() == UserData.getDefaultInstance()
    }

    suspend fun login(username: String, password: String): String {
        val response = api.login(username, password)
/*
        userDataService.storeUserData(
            username,
            password,
            response.token,
            response.refreshToken
        )
*/

        store.token = response.token
        store.username = username

        return username
    }

    suspend fun loginWithSavedCredentials(): String {
        val (username, password) = userDataService.getCredentials()
        return login(username, password)
    }

    /**
     * If refresh token fails (maybe expired), we try to login with saved credentials
     */
    suspend fun refreshToken() {
        val refreshToken = userDataService.getRefreshToken()
        try {
            val response = api.refreshToken(refreshToken)
/*
        userDataService.storeUserData(
            response.token,
            response.refreshToken
        )
*/
            store.token = response.token
        } catch (e: PenguStoreApiException) {
            loginWithSavedCredentials()
        }
    }

    suspend fun logout() {
        userDataService.storeUserData(token = "", refreshToken = "")
        store.token = ""
        store.username = ""
    }

    suspend fun registerGuest(): String {
        val username = UUID.randomUUID()
            .toString()
            .filter { it.isLetterOrDigit() }
            .substring(startIndex = 0, 16)
        val response = api.registerGuest(username)
/*
        userDataService.storeUserData(
            username,
            response.password,
            response.token,
            response.refreshToken
        )
*/

        store.token = response.token
        store.username = username

        return username
    }
}