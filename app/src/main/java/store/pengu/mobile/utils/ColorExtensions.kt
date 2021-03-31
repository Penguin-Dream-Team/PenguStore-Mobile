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
