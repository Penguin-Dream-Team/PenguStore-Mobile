package store.pengu.mobile.utils

import android.content.Context
import androidx.compose.ui.unit.Dp
import store.pengu.mobile.R

object Math {
    fun max(a: Dp, b: Dp) = if (a > b) a else b
    fun min(a: Dp, b: Dp) = if (a < b) a else b
}
