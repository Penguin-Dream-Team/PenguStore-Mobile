package store.pengu.mobile.utils

import androidx.compose.ui.unit.Dp

object Math {
    fun max(a: Dp, b: Dp) = if (a > b) a else b
    fun min(a: Dp, b: Dp) = if (a < b) a else b
}
