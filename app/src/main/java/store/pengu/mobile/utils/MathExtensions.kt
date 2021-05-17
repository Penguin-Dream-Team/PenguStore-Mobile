package store.pengu.mobile.utils

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import store.pengu.mobile.R

object Math {
    fun max(a: Dp, b: Dp) = if (a > b) a else b
    fun min(a: Dp, b: Dp) = if (a < b) a else b

    /*fun secondsToMinutes(seconds: Int): String {
        if (seconds == 0) return stringResource(R.string.zero_seconds)
        if (seconds < 60) return getSeconds(seconds % 60)
        return getMinutes(seconds / 60) + context.getString(R.string.and) + getSeconds(seconds % 60)
    }

    private fun getMinutes(minutes: Int): String {
        if (minutes == 1) return minutes.toString() + stringResource(R.string.minute)
        return minutes.toString() + stringResource(R.string.minutes)
    }

    private fun getSeconds(seconds: Int): String {
        if (seconds == 0) return ""
        if (seconds == 1) return stringResource(R.string.one_second)
        return seconds.toString() + stringResource(R.string.seconds)
    }*/
}
