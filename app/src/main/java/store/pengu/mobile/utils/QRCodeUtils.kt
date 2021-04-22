package store.pengu.mobile.utils

object QRCodeUtils {

    fun generateQRCodeUrl(data: String): String {
        return "https://chart.googleapis.com/chart?cht=qr&chs=150x150&chl=$data"
    }
}