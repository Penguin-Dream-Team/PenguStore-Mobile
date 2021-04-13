package store.pengu.mobile.views.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import store.pengu.mobile.services.AccountService

@SuppressLint("RestrictedApi")
@ExperimentalComposeUiApi
@Composable
fun LoginScreenForm(
    accountService: AccountService,
    canRegister: MutableState<Boolean>,
    attemptLogin: (suspend () -> String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .fillMaxWidth()
    ) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var canLogin by remember { mutableStateOf(false) }

        val submit: () -> Unit = {
            attemptLogin {
                accountService.login(username, password)
            }
        }

        val focusRequester = FocusRequester()

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                canLogin = username.isNotBlank() && password.isNotBlank()
            },
            placeholder = { Text("Username") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusRequester.requestFocus()
            }),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "username")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                canLogin = username.isNotBlank() && password.isNotBlank()
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(onGo = {
                submit()
            }),
            placeholder = { Text("Password") },
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.VpnKey, contentDescription = "password")
            }
        )

        Button(
            onClick = submit, enabled = canLogin && canRegister.value,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 75.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Login")
        }
    }
}