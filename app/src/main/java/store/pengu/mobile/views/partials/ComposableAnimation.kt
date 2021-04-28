package store.pengu.mobile.views.partials

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@ExperimentalAnimationApi
fun NavGraphBuilder.animatedComposable(route: String, content: @Composable () -> Unit) {
    composable(route) {
        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally(
                initialOffsetX = { -150 }
            ) + expandHorizontally(
                expandFrom = Alignment.End
            ) + fadeIn(initialAlpha = 0.3f),
            exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut(),
            initiallyVisible = false
        ) {
            content()
        }
    }
}
