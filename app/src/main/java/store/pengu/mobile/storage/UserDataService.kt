package store.pengu.mobile.storage

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import store.pengu.mobile.storage.proto.UserData
import java.io.IOException

class UserDataService(
    private val dataStore: DataStore<UserData>
) {
    private val userDataFlow: Flow<UserData> = dataStore.data.catch { e ->
        if (e is IOException) {
            Log.e("UserDataService", "Error reading sort order preferences.", e)
            emit(UserData.getDefaultInstance())
        } else {
            throw e
        }
    }

    suspend fun getUserData(): UserData =
        userDataFlow.firstOrNull() ?: UserData.getDefaultInstance()

    suspend fun getCredentials(): Pair<String, String> {
        val userData = getUserData()
        return userData.username to userData.password
    }

    suspend fun getUsername(): String {
        val userData = getUserData()
        return userData.username
    }

    suspend fun getToken(): String {
        val userData = getUserData()
        return userData.token
    }

    suspend fun isLoggedIn(): Boolean {
        val userData = getUserData()
        return userData.token.isNotBlank()
    }

    suspend fun isGuest(): Boolean {
        val userData = getUserData()
        return userData.guest
    }

    suspend fun storeUserData(
        username: String? = null,
        password: String? = null,
        token: String? = null,
        guest: Boolean? = null,
        email: String? = null,
    ) {
        dataStore.updateData {
            val userDataBuilder = it.toBuilder()

            if (username != null) {
                userDataBuilder.username = username
            }

            if (password != null) {
                userDataBuilder.password = password
            }

            if (token != null) {
                userDataBuilder.token = token
            }

            if (guest != null) {
                userDataBuilder.guest = guest
            }

            if (email != null) {
                userDataBuilder.email = email
            }

            userDataBuilder.build()
        }
    }
}

val Context.userDataStore: DataStore<UserData> by dataStore(
    fileName = "user_data.pb",
    serializer = UserDataSerializer
)