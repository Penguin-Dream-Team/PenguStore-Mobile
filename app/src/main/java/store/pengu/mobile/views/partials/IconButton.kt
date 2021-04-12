package store.pengu.mobile.views.partials

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    selected: Boolean = false
) {
    IconButton(onClick) {
        Icon(
            icon,
            description,
            tint = if (selected) MaterialTheme.colors.primary else LocalContentColor.current
        )
    }
}