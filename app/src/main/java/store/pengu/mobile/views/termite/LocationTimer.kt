package store.pengu.mobile.views.termite

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.services.MapsService
import store.pengu.mobile.services.TermiteService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController
import store.pengu.mobile.utils.launcherForActivityResult
import store.pengu.mobile.views.MainActivity
import store.pengu.mobile.views.MainActivity.Companion.REQUEST_CHECK_SETTINGS
import store.pengu.mobile.views.loading.RequestLocation
import store.pengu.mobile.views.loading.RequestLocationCallback

@SuppressLint("MissingPermission")
private fun requestLocation(
    mapsService: MapsService,
    store: StoreState,
    termiteService: TermiteService,
    locationRequest: LocationRequest,
    setEnabled: (Boolean) -> Unit
) {
    val fusedLocationClient = mapsService.fusedLocationProviderClient

    val locationCallback = RequestLocationCallback(store.setLocation)

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.myLooper()
    )
    termiteService.wifiDirectON()
    setEnabled(true)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LocationTimer(
    mapsService: MapsService,
    store: StoreState,
    snackbarController: SnackbarController,
    termiteService: TermiteService,
    mainActivity: MainActivity,
    tryAgain: Boolean,
    setEnabled: (Boolean) -> Unit
) {
    val context = LocalContext.current

    val locationRequest by remember {
        mutableStateOf(LocationRequest.create().apply {
            fastestInterval = 1500
            interval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        })
    }
    val errorStringResource = stringResource(R.string.error_current_location)
    var token by remember { mutableStateOf(false) }

    val launcher =
        launcherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                snackbarController.showDismissibleSnackbar(errorStringResource)
            } else {
                token = !token
                requestLocation(mapsService, store, termiteService, locationRequest, setEnabled)
            }
        }

    LaunchedEffect(tryAgain, token) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                val result = LocationServices.getSettingsClient(context).checkLocationSettings(
                    LocationSettingsRequest.Builder().apply {
                        addLocationRequest(locationRequest)
                        setNeedBle(true)
                        setAlwaysShow(true)
                    }.build()
                )

                result.addOnSuccessListener {
                    when (PackageManager.PERMISSION_GRANTED) {
                        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                            requestLocation(mapsService, store, termiteService, locationRequest, setEnabled)
                        }
                        else -> {
                            launch {
                                // needs to wait for launcher to finish initializing
                                delay(100L)
                                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    }
                }

                result.addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        exception.startResolutionForResult(
                            mainActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    }
                }
            }
            else -> {
                // needs to wait for launcher to finish initializing
                delay(100L)
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

    }
}