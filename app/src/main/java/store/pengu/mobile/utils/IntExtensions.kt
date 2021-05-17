package store.pengu.mobile.utils

import android.content.Context
import store.pengu.mobile.R
import java.text.NumberFormat
import java.util.*

fun Int.toEuros(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("EUR")
    return format.format(this)
}

fun String.euroToInt(): Int = this.filter { it.isDigit() }.toInt()

fun String.pluralize(amount: Int): String = if (amount == 1) this else "${this}s"

fun Int.secondsToMinutes(context: Context): String {
    if (this == 0) return context.getString(R.string.zero_seconds)
    if (this < 60) return getSeconds(context, this % 60)
    return getMinutes(context, this / 60) + context.getString(R.string.and) + getSeconds(
        context,
        this % 60
    )
}

private fun getMinutes(context: Context, minutes: Int): String {
    if (minutes == 1) return minutes.toString() + context.getString(R.string.minute)
    return minutes.toString() + context.getString(R.string.minutes)
}

private fun getSeconds(context: Context, seconds: Int): String {
    if (seconds == 0) return ""
    if (seconds == 1) return context.getString(R.string.one_second)
    return seconds.toString() + context.getString(R.string.seconds)
}
