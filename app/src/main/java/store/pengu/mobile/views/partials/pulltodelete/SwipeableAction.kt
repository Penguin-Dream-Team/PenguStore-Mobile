package store.pengu.mobile.views.partials.pulltodelete

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.CombinedModifier
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private const val MAX_OFFSET = 150f
private const val MIN_REFRESH_OFFSET = 100f

/**
 * A layout composable with [content].
 *
 * Example usage:
 *
 * @param modifier The modifier to be applied to the layout.
 * @param maxOffset How many pixels can the progress indicator can be dragged right
 * @param minTriggerOffset Minimum drag value to trigger [onAction]
 * @param isPerforming Flag describing if [SwipeableAction] is performing.
 * @param onAction Callback to be called if layout is pulled.
 * @param onCancelAction Callback to be called if layout is reset.
 * @param content The content of the [SwipeableAction].
 */
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun SwipeableAction(
    modifier: Modifier = Modifier,
    maxOffset: Float = MAX_OFFSET,
    minTriggerOffset: Float = MIN_REFRESH_OFFSET,
    icon: ImageVector,
    isPerforming: Boolean,
    enabled: Boolean,
    onAction: () -> Unit,
    onCancelAction: () -> Unit,
    content: @Composable () -> Unit
) {

    Box(
        modifier = CombinedModifier(
            inner = Modifier
                .clip(RectangleShape),
            outer = modifier
        )
    ) {

        val bgColor = Color.Red
        Surface(
            color = bgColor,
            contentColor = contentColorFor(backgroundColor = bgColor),
            shape = RoundedCornerShape(topStartPercent = 5, bottomStartPercent = 5),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Delete",
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 15.dp)
            )
        }

        val offsetX = remember { mutableStateOf(0f) }
        val transitionState = remember {
            MutableTransitionState(isPerforming).apply {
                targetState = !isPerforming
            }
        }

        val transition = updateTransition(transitionState, null)
        val offsetTransition by transition.animateFloat(
            label = "cardOffsetTransition",
            transitionSpec = { tween(durationMillis = 300) },
            targetValueByState = {
                if (isPerforming) maxOffset - offsetX.value else -offsetX.value
            },
        )

        Box(
            modifier = Modifier
                .offset { IntOffset((offsetX.value + offsetTransition).roundToInt(), 0) }
                .pointerInput(Unit) {
                    Log.d("hello", enabled.toString())
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (enabled) {
                            val original = Offset(offsetX.value, 0f)
                            val summed = original + Offset(x = dragAmount, y = 0f)
                            val newValue = Offset(x = summed.x.coerceIn(0f, maxOffset), y = 0f)
                            if (newValue.x >= minTriggerOffset) {
                                onAction()
                                return@detectHorizontalDragGestures
                            } else if (newValue.x <= 0) {
                                onCancelAction()
                                return@detectHorizontalDragGestures
                            }
                            change.consumePositionChange()
                            offsetX.value = newValue.x
                        } else {
                            Log.d("Hello", "HELl")
                            offsetX.value = 0f
                        }
                    }
                }
        ) {
            content()
        }
    }
}