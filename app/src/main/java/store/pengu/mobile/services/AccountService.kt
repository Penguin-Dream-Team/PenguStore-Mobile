package store.pengu.mobile.services

import android.annotation.SuppressLint
import android.util.Log
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import store.pengu.mobile.api.PenguStoreApi
import store.pengu.mobile.errors.PenguStoreApiAuthorizationException
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.storage.UserDataService
import store.pengu.mobile.storage.proto.UserData
import java.util.*

class AccountService(
    private val userDataService: UserDataService,
    private val api: PenguStoreApi,
    private val store: StoreState,
) {

    var navController: NavHostController? = null

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

    suspend fun hasSavedCredentials(): Boolean {
        return userDataService.getUserData() != UserData.getDefaultInstance()
    }

    suspend fun login(
        username: String,
        password: String,
        guest: Boolean = false,
        getProfile: Boolean = true
    ): String {
        val response = api.login(username, password)
        userDataService.storeUserData(
            username,
            password,
            response.token,
        )

        store.token = response.token
        store.username = username
        store.guest = guest

        // reload profile data
        if (getProfile) {
            getProfile()
        }

        return username
    }

    suspend fun loginWithSavedCredentials(): String {
        val (username, password) = userDataService.getCredentials()
        val guest = userDataService.isGuest()
        return login(username, password, guest)
    }

    suspend fun getProfile() {
        retryWithLogin(false) {
            val profile = api.getProfile()
            store.username = profile.username
            store.email = profile.email ?: ""
            store.guest = profile.guest

            userDataService.storeUserData(
                username = profile.username,
                email = profile.email,
                guest = profile.guest,
            )
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

            userDataService.storeUserData(
                username = profile.username,
                email = profile.email,
                guest = profile.guest,
            )

        } catch (e: PenguStoreApiAuthorizationException) {
            loginWithSavedCredentials()
            if (retry) {
                updateAccount(username, password, email, retry = false)
            }
        }
    }

    suspend fun logout() {
        userDataService.storeUserData(token = "")
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

        userDataService.storeUserData(
            username,
            response.password,
            response.token,
        )

        store.token = response.token
        store.username = username
        store.guest = true

        return username
    }

    @SuppressLint("RestrictedApi")
    suspend fun <T> retryWithLogin(
        getProfile: Boolean = true,
        request: suspend (Boolean) -> T
    ): T? {
        return try {
            request(getProfile)
        } catch (e: PenguStoreApiAuthorizationException) {
            store.token = ""
            if (hasSavedCredentials()) {
                try {
                    loginWithSavedCredentials()
                    return request(getProfile)
                } catch (e: PenguStoreApiException) {
                    userDataService.storeUserData(null, null, null, null, null)
                } catch (e: PenguStoreApiAuthorizationException) {
                    userDataService.storeUserData(null, null, null, null, null)
                }
            }

            navController?.navigate("login")
            navController?.backStack?.clear()
            return null
        }
    }
}