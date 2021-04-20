package store.pengu.mobile.views.lists

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import kotlin.math.roundToInt

enum class AvailableListColor(
    private val red: Int,
    private val green: Int,
    private val blue: Int,
    private val alpha: Float = 1.0f
) {
    RED(231, 76, 60),
    BLUE(52, 152, 219),
    YELLOW(241, 196, 15),
    GREEN(46, 204, 112),
    ORANGE(230, 125, 34),
    PURPLE(156, 89, 182),
    SILVER(189, 195, 199);

    fun getName(): String = name.toLowerCase(Locale.current).capitalize(Locale.current)

    fun toColor() =
        Color(red = red, green = green, blue = blue, alpha = (alpha * 255).roundToInt())
}
