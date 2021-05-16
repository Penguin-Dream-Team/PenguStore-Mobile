package store.pengu.mobile.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import coil.request.ImageResult
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

    suspend fun getBitmapImage(context: Context, image: String): Bitmap? {
        val bytes = getImageBytes(context, image)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    suspend fun getEncodedImage(context: Context, image: String): String {
        return encodeImage(getImageBytes(context, image))
    }
}
