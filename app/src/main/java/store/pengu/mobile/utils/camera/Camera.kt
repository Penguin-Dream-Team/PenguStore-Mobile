package store.pengu.mobile.utils.camera

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri.encode
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import io.ktor.http.*
import io.ktor.util.*
import store.pengu.mobile.R
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@KtorExperimentalAPI
class Camera {

    private var previewView: PreviewView? = null
    private var analysisUseCase: ImageAnalysis? = null

    companion object {

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private var toastTimeout: Int = -1

    private val screenAspectRatio: Int
        get() {
            // Get screen metrics used to setup camera for full screen resolution
            val metrics = DisplayMetrics().also { previewView?.display?.getRealMetrics(it) }
            return aspectRatio(metrics.widthPixels, metrics.heightPixels)
        }

    /**
     *  [androidx.camera.core.ImageAnalysis],[androidx.camera.core.Preview] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    @SuppressLint("InflateParams")
    @Composable
    fun CameraPreview(
        navController: NavHostController,
        storeState: StoreState,
        productsService: ProductsService
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            Text(
                text = "Scan the barcode to share this pantry",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            AndroidView(factory = {
                LayoutInflater.from(it).inflate(R.layout.camera_layout, null)
            }) { inflatedLayout ->
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    bindAnalysis(
                        lifecycleOwner,
                        inflatedLayout as PreviewView,
                        cameraProvider,
                        navController,
                        storeState,
                        productsService,
                        context
                    )
                }, ContextCompat.getMainExecutor(context))
            }
        }
    }

    @KtorExperimentalAPI
    private fun bindAnalysis(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        cameraProvider: ProcessCameraProvider,
        navController: NavHostController,
        storeState: StoreState,
        productsService: ProductsService,
        context: Context
    ) {
        val preview: Preview = Preview.Builder().build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)

        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase)
        }

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(previewView.display.rotation)
            .build()
        val cameraExecutor = Executors.newSingleThreadExecutor()

        analysisUseCase?.setAnalyzer(cameraExecutor, { imageProxy ->
            processImageProxy(
                barcodeScanner,
                imageProxy,
                navController,
                storeState,
                productsService,
                context
            )
        })

        try {
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, analysisUseCase)
        } catch (illegalStateException: IllegalStateException) {
            illegalStateException.message?.let { Log.e(ContentValues.TAG, it) }
        } catch (illegalArgumentException: IllegalArgumentException) {
            illegalArgumentException.message?.let { Log.e(ContentValues.TAG, it) }
        }
    }

    @KtorExperimentalAPI
    @SuppressLint("UnsafeExperimentalUsageError", "RestrictedApi", "UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,
        navController: NavHostController,
        store: StoreState,
        productsService: ProductsService,
        context: Context
    ) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach { barcode ->
                    barcode.rawValue?.let {
                        val parameters = URLBuilder(it).parameters

                        productsService.addBarcodeProduct(parameters.toString())
                        Toast.makeText(context, "Added Barcode to Product ${store.selectedProduct}", Toast.LENGTH_SHORT).show()

                        imageProxy.close()
                        barcodeScanner.close()
                        navController.popBackStack()

                        if (toastTimeout >= 0)
                            toastTimeout -= 1
                    }
                }
            }.addOnCompleteListener {
                imageProxy.close()
            }
    }
}