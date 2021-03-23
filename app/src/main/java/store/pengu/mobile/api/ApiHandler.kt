package store.pengu.mobile.api

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.Config
import java.text.DateFormat
import java.util.concurrent.TimeoutException

abstract class ApiHandler(open val store: StoreState) {

    companion object {
        private val APIConfig = Config.load()
        val PROTOCOL = APIConfig.protocol
        val HOST = APIConfig.host
        val PORT = APIConfig.port
        val SECURE_PORT = APIConfig.secure_port
        val TIMEOUT = APIConfig.timeout
    }

    protected val api = HttpClient(Android) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                enable(SerializationFeature.INDENT_OUTPUT)
                dateFormat = DateFormat.getDateInstance()
                acceptContentTypes = listOf(ContentType.Application.Json)
            }
        }

        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
            url.host = HOST
            url.protocol = if (PROTOCOL == "https") URLProtocol.HTTPS else URLProtocol.HTTP
            url.port = if (PROTOCOL == "https") SECURE_PORT else PORT
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT
        }
    }

    protected suspend inline fun <reified T> get(path: String): T =
        try {
            api.get(path = path) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> post(path: String, data: Any): T =
        try {
            api.post(path = path, body = data) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend fun handleApiException(e: Exception) {
        val err = when (e) {
            is HttpRequestTimeoutException,
            is SocketTimeoutException,
            is java.net.SocketTimeoutException,
            is TimeoutException,
            is ConnectTimeoutException -> Response.ErrorResponse("Can't reach the server")
            is ClientRequestException -> Response.ErrorResponse(e.response.receive())
            else -> {
                e.printStackTrace()
                Response.ErrorResponse(e.message ?: "Unknown error")
            }
        }

        throw PenguStoreApiException(err.message)
    }

    protected fun addJWTTokenToRequest(headers: HeadersBuilder) {
        if (store.token.isNotBlank()) headers["Authorization"] = "Bearer ${store.token}"
    }
}