package store.pengu.mobile.api

import store.pengu.mobile.api.requests.LoginRequest
import store.pengu.mobile.api.requests.SetupRequest
import store.pengu.mobile.data.User
import store.pengu.mobile.states.StoreState

class PenguStoreApi(
    store: StoreState
) : ApiHandler(store) {

    suspend fun login(loginRequest: LoginRequest): Response.SuccessResponse<User> =
        post(Routes.LOGIN, loginRequest)

    suspend fun setup(): Response.SuccessResponse<User> {
        val setupRequest = SetupRequest(phonePublicKey = "DUMMY")
        return post(Routes.SETUP, setupRequest)
    }

    suspend fun dashboard(): Response.SuccessResponse<String> = get(Routes.DASHBOARD)
}