package store.pengu.mobile.utils

import androidx.compose.ui.graphics.Color

fun Color.lighten(amount: Float) =
    copy(
        red = red + amount,
        green = green + amount,
        blue = blue + amount
    )

fun Color.darken(amount: Float) =
    copy(
        red = red - amount,
        green = green - amount,
        blue = blue - amount
    )

fun String.toColor() = split(" ").run {
     Color(get(0).toInt(), get(1).toInt(), get(2).toInt())
}