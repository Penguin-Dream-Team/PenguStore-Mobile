package store.pengu.mobile.services

import android.util.Log
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.errors.PenguStoreApiAuthorizationException
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.storage.UserDataService
import store.pengu.mobile.storage.proto.UserData
import java.util.*

class AccountService(
    private val userDataService: UserDataService,
    private val api: PenguStoreApi,
    private val store: StoreState
) {

    suspend fun loadData() {
        val userData = userDataService.getUserData()
        store.token = userData.token
        store.username = userData.username
        store.guest = userData.guest
        store.email = userData.email

        // If logged in, refresh profile
        if (store.isLoggedIn()) {
            getProfile()
        }
    }

    suspend fun needsLogin(): Boolean {
        return userDataService.getUserData() == UserData.getDefaultInstance()
    }

    suspend fun login(username: String, password: String, guest: Boolean = false): String {
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
        store.guest = guest

        // reload profile data
        getProfile()

        return username
    }

    suspend fun loginWithSavedCredentials(): String {
        val (username, password) = userDataService.getCredentials()
        val guest = userDataService.isGuest()
        return login(username, password, guest)
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

            // reload profile data
            getProfile()
        } catch (e: PenguStoreApiAuthorizationException) {
            loginWithSavedCredentials()
        }
    }

    suspend fun getProfile(retry: Boolean = true) {
        try {
            Log.d("DIIIIIIS", "hello there")
            val profile = api.getProfile()
            Log.d("DIIIIIIS", profile.toString())
            store.username = profile.username
            store.email = profile.email ?: ""
            store.guest = profile.guest
/*
        userDataService.storeUserData(
            username = profile.username,
            email = profile.email,
            guest = profile.guest,
        )
*/
        } catch (e: PenguStoreApiAuthorizationException) {
            refreshToken()
            if (retry) {
                getProfile(retry = false)
            }
        }
    }

    suspend fun updateAccount(
        username: String? = null,
        password: String? = null,
        email: String? = null,
        retry: Boolean = true
    ) {
        try {
            val profile = api.updateAccount(username, password, email)
            store.username = profile.username
            store.email = profile.email ?: ""
            store.guest = profile.guest
/*
        userDataService.storeUserData(
            username = profile.username,
            email = profile.email,
            guest = profile.guest,
        )
*/
        } catch (e: PenguStoreApiAuthorizationException) {
            refreshToken()
            if (retry) {
                updateAccount(username, password, email, retry = false)
            }
        }
    }

    suspend fun logout() {
        userDataService.storeUserData(token = "", refreshToken = "")
        store.token = ""
        store.username = ""
        store.guest = true
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
        store.guest = true

        return username
    }
}