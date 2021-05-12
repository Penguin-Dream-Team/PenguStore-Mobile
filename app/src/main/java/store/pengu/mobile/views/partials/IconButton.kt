package store.pengu.mobile.views.partials

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    selected: Boolean = false,
    enabled: Boolean = true
) {
    IconButton(onClick, enabled = enabled) {
        Icon(
            icon,
            description,
            tint = if (selected) MaterialTheme.colors.primary else LocalContentColor.current,
            modifier = if (!enabled) Modifier.alpha(0.3f) else Modifier.alpha(1.0f)
        )
    }
}