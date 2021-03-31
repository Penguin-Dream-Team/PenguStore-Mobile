package store.pengu.mobile.views.animations

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

object ShimmerAnimationDefinitions {

    fun calcGradientWidth(height: Float): Float {
        return 0.2f * height
    }

    @Composable
    fun animateOffsets(
        width: Float,
        height: Float,
        gradientWidth: Float = calcGradientWidth(height),
    ): Pair<Float, Pair<Float, Float>> {
        val transition = rememberInfiniteTransition()
        val xOffset by transition.animateFloat(
            initialValue = 0f, //- gradientWidth,
            targetValue = width + gradientWidth,
            animationSpec = infiniteRepeatable(
                animation = tween(1300, delayMillis = 300, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val yOffset by transition.animateFloat(
            initialValue = 0f, // - gradientWidth,
            targetValue = height + gradientWidth,
            animationSpec = infiniteRepeatable(
                animation = tween(1300, delayMillis = 300, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        return gradientWidth to Pair(xOffset, yOffset)
    }
}