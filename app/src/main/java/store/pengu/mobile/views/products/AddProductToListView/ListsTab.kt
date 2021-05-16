@file:Suppress("PackageName")

package store.pengu.mobile.views.products.AddProductToListView

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.data.UserList
import store.pengu.mobile.data.productlists.ProductListEntry
import store.pengu.mobile.views.partials.pulltorefresh.PullToRefresh

@ExperimentalAnimationApi
@Composable
fun <T : ProductListEntry> ListsTab(
    visible: Boolean,
    lists: List<T>,
    onRefresh: suspend () -> Unit,
    renderItem: @Composable (T, Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = visible,
    ) {
        var isRefreshing: Boolean by remember { mutableStateOf(false) }
        var needsRefresh: Boolean by remember { mutableStateOf(true) }
        val refresh = {
            isRefreshing = true
            coroutineScope.launch(Dispatchers.IO) {
                onRefresh()
                isRefreshing = false
            }
        }

        if (needsRefresh && !isRefreshing) {
            refresh()
            needsRefresh = false
        }

        PullToRefresh(
            isRefreshing = isRefreshing,
            onRefresh = {
                refresh()
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .fillMaxSize()
            ) {
                if (lists.isEmpty() && !isRefreshing) {
                    item {
                        Text(stringResource(R.string.empty_list_info))
                    }
                }
                items(items = lists) { item ->
                    renderItem(item, !isRefreshing)
                }
            }
        }
    }
}