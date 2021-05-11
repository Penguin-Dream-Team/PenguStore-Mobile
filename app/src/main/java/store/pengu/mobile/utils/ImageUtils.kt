package store.pengu.mobile.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import java.io.ByteArrayOutputStream

object ImageUtils {
    private suspend fun getImageBytes(context: Context, image: String): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        Coil.execute(
            ImageRequest.Builder(context)
                .data(image)
                .target {
                    it.toBitmap()
                        .compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                }
                .build()
        )

        return byteArrayOutputStream.toByteArray()
    }

    private fun encodeImage(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    suspend fun getEncodedImage(context: Context, image: String): String {
        return encodeImage(getImageBytes(context, image))
    }
}
