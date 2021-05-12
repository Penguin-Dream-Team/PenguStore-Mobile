package store.pengu.mobile.services

import android.content.Context
import android.content.pm.PackageManager
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import store.pengu.mobile.data.DistanceInfo
import java.text.DateFormat

@Suppress("PrivatePropertyName")
class MapsService(
    context: Context,
) {

    private var API_KEY: String = context.packageManager.getApplicationInfo(
        context.packageName,
        PackageManager.GET_META_DATA
    ).metaData.getString("com.google.android.geo.API_KEY") ?: ""

    val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @JsonDeserialize
    private data class DistanceInfoResponse(
        val destinationAddresses: List<String>,
        val originAddresses: List<String>,
        val rows: List<DistanceInfoResponseElementList>,
        val status: String
    )

    @JsonDeserialize
    private data class DistanceInfoResponseElementList(
        val elements: List<DistanceInfoResponseElement>
    )

    @JsonDeserialize
    private data class DistanceInfoResponseElement(
        val distance: DistanceInfoResponseElementValue?,
        val duration: DistanceInfoResponseElementValue?,
        val status: String
    )

    @JsonDeserialize
    private data class DistanceInfoResponseElementValue(
        val text: String,
        val value: Int,
    )

    companion object {
        private const val API_URL =
            "https://maps.googleapis.com/maps/api/distancematrix/json?mode=driving&language=en&avoid=tolls"
    }

    private val client = HttpClient(Android) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                enable(SerializationFeature.INDENT_OUTPUT)
                dateFormat = DateFormat.getDateInstance()
                acceptContentTypes = listOf(ContentType.Application.Json)
                propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 3000
        }
    }

    suspend fun getDistanceInfo(source: LatLng, destination: LatLng): DistanceInfo {
        val response = client.get<DistanceInfoResponse>(API_URL) {
            parameter("origins", "${source.latitude}, ${source.longitude}")
            parameter("destinations", "${destination.latitude}, ${destination.longitude}")
            parameter("language", "en-US")
            parameter("key", API_KEY)
        }

        val info = response.rows.firstOrNull()?.elements?.firstOrNull()?.run {
            duration?.let {
                distance?.let {
                    return@run this
                }
            }
            this
        } ?: throw RuntimeException("Distance not found")

        return DistanceInfo(
            duration = info.duration!!.text,
            distance = info.distance!!.text
        )
    }

}