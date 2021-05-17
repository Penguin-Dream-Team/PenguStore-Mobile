package store.pengu.mobile.views.products.ProductInfo.bottommenu

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import io.ktor.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.ProductsService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController

@KtorExperimentalAPI
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun EditProductBottomSheetMenu(
    productsService: ProductsService,
    store: StoreState,
    snackbarController: SnackbarController,
    closeMenu: (String?) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val updatedProduct = stringResource(R.string.updated_product)

    val actionWrapper: (String, suspend () -> Unit) -> Unit = { successMessage, callable ->
        coroutineScope.launch {
            try {
                callable()
                snackbarController.showDismissibleSnackbar(successMessage)
            } catch (e: PenguStoreApiException) {
                snackbarController.showDismissibleSnackbar(e.message)
            }
            closeMenu(null)
        }
    }

    val product = store.selectedProduct
    if (product != null) {
        ProductFormBottomSheetMenu(
            product,
            { closeMenu(null) },
            onSave = { name, barcode ->
                actionWrapper(updatedProduct) {
                    productsService.editProduct(product.id, name, barcode)
                }
            },
            onEditLists = {
                closeMenu("add_product_to_list/${product.id}")
            },
            onUploadImage = {
            }
        )
    }
}
