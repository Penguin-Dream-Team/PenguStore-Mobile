package store.pengu.mobile.views.partials

import android.os.Bundle
import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument

@ExperimentalAnimationApi
fun NavGraphBuilder.animatedComposable(route: String, arguments: List<NamedNavArgument> = emptyList(), content: @Composable (Bundle?) -> Unit) {
    composable(route, arguments) {
        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally(
                initialOffsetX = { -50 }
            ) + fadeIn(initialAlpha = 0.0f),
            exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut(),
            initiallyVisible = false
        ) {
            content(it.arguments)
        }
    }
}
