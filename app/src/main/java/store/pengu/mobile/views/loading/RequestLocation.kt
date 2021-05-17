package store.pengu.mobile.views.loading

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.services.MapsService
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.launcherForActivityResult
import store.pengu.mobile.views.partials.pulltorefresh.LoadingProgressIndicator

class RequestLocationCallback(
    private val setLocation: (LatLng?) -> Unit,
    private val setLoading: (Boolean) -> Unit = {}
) : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)

        setLocation(null)
        locationResult.locations.lastOrNull()?.let {
            setLocation(LatLng(it.latitude, it.longitude))
            Log.d("HELLPPP: CALLKBACK", it.toString())
        }
        setLoading(false)
    }
}

@ExperimentalAnimationApi
@Composable
fun RequestLocation(
    snackbarController: SnackbarController,
    loading: Boolean,
    setLoading: (Boolean) -> Unit,
    setLocation: (LatLng?) -> Unit,
    mapsService: MapsService,
    showLoading: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var canGetLocation by remember { mutableStateOf(false) }
    var needsLocationPermission by remember { mutableStateOf(true) }

    val cannotFetchCurrentLocation = stringResource(R.string.cannot_fetch_current_location)

    val launcher =
        launcherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                snackbarController.showDismissibleSnackbar(cannotFetchCurrentLocation)
                setLoading(false)
            }
            canGetLocation = granted
        }

    AnimatedVisibility(visible = loading, modifier = Modifier.fillMaxSize()) {
        if (needsLocationPermission) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    canGetLocation = true
                }
                else -> {
                    coroutineScope.launch {
                        // needs to wait for launcher to finish initializing
                        delay(100L)
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
            needsLocationPermission = false
        }

        if (canGetLocation) {
            val fusedLocationClient = mapsService.fusedLocationProviderClient

            val locationRequest = LocationRequest.create().apply {
                numUpdates = 2
                fastestInterval = 200
                interval = 200
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            }
            val locationCallback = remember { RequestLocationCallback(setLocation, setLoading) }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }

        if (showLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadingProgressIndicator(
                    progressColor = MaterialTheme.colors.primary,
                    backgroundColor = MaterialTheme.colors.surface
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = stringResource(R.string.loading)
                )
            }
        }
    }
}