package store.pengu.mobile.utils

import androidx.compose.ui.unit.Dp

object Math {
    fun max(a: Dp, b: Dp) = if (a > b) a else b
    fun min(a: Dp, b: Dp) = if (a < b) a else b

    fun secondsToMinutes(seconds: Int): String {
        if (seconds == 0) return "0 seconds"
        if (seconds < 60) return getSeconds(seconds % 60)
        return getMinutes(seconds / 60) + " and " + getSeconds(seconds % 60)
    }

    private fun getMinutes(minutes: Int): String {
        if (minutes == 1) return "$minutes minute"
        return "$minutes minutes"
    }

    private fun getSeconds(seconds: Int): String {
        if (seconds == 0) return ""
        if (seconds == 1) return "1 second"
        return "$seconds seconds"
    }
}
