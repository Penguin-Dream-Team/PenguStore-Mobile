package store.pengu.mobile.utils

import java.text.NumberFormat
import java.util.*

fun Int.toEuros(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("EUR")
    return format.format(this)
}

fun String.euroToInt(): Int = this.filter { it.isDigit() }.toInt()