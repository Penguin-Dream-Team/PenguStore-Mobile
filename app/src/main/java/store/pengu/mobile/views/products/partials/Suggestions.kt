package store.pengu.mobile.views.products.partials

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import store.pengu.mobile.R
import store.pengu.mobile.api.responses.lists.UserListType
import store.pengu.mobile.data.Product

@Composable
fun Suggestions(navController: NavController, product: Product, pantryId: Long, setShowSuggestion: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { setShowSuggestion(false) },
        text = {
            Text(text = stringResource(R.string.suggestion_text) + product.name)
        },
        dismissButton = {
            Button(
                onClick = {  setShowSuggestion(false) }
            ) {
                Text(stringResource(R.string.close))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    navController.navigate("add_product_to_list/${product.id}?listType=${UserListType.PANTRY.ordinal}&listId=$pantryId")
                    setShowSuggestion(false)
                }
            ) {
                Text(stringResource(R.string.add_product))
            }
        }
    )
}