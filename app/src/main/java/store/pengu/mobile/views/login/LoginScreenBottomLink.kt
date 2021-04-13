package store.pengu.mobile.views.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonDefaults.outlinedButtonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import store.pengu.mobile.services.AccountService

@SuppressLint("RestrictedApi")
@Composable
fun LoginScreenBottomLink(
    accountService: AccountService,
    canRegister: MutableState<Boolean>,
    attemptLogin: (suspend () -> String) -> Unit
) {

    Button(
        onClick = {
            attemptLogin {
                accountService.registerGuest()
            }
        },
        contentPadding = PaddingValues(16.dp),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
        colors = outlinedButtonColors(backgroundColor = Color.Transparent),
        modifier = Modifier.padding(bottom = 16.dp),
        enabled = canRegister.value
    ) {
        Text(
            text = "Continue without account",
            textDecoration = TextDecoration.Underline
        )
    }
}
