package store.pengu.mobile.views.profile

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.AccountService
import store.pengu.mobile.states.StoreState
import store.pengu.mobile.utils.SnackbarController

suspend fun handleSubmission(
    creating: MutableState<Boolean>,
    submitting: MutableState<Boolean>,
    canSubmit: Boolean,
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    snackbarController: SnackbarController,
    accountService: AccountService,
    isRegistering: Boolean
) {
    if (!creating.value) {
        creating.value = true
        return
    }

    if (submitting.value || !canSubmit) {
        return
    }

    submitting.value = true

    // if not, then its just updating
    if (isRegistering) {
        if (username.isBlank()) {
            snackbarController.showDismissibleSnackbar("You need to choose a username")
            return
        }
        if (email.isBlank()) {
            snackbarController.showDismissibleSnackbar("You need to choose an email")
            return
        }
        if (password.isBlank()) {
            snackbarController.showDismissibleSnackbar("You need to choose a password")
            return
        }
    }

    if (password != confirmPassword) {
        snackbarController.showDismissibleSnackbar("Passwords don't match")
        return
    }

    try {
        accountService.updateAccount(username, email, password)
        snackbarController.showDismissibleSnackbar(
            if (isRegistering) "Registered successfully"
            else "Updated successfully"
        )
    } catch (e: PenguStoreApiException) {
        snackbarController.showDismissibleSnackbar(e.message)
    }
    submitting.value = false
}

fun canSubmitValues(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    guest: Boolean
): Boolean {
    return if (guest) {
        username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && password == confirmPassword
    } else {
        username.isNotBlank() && email.isNotBlank() && password == confirmPassword
    }
}

@ExperimentalComposeUiApi
@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@Composable
fun ProfileScreenAccountCreation(
    accountService: AccountService,
    snackbarController: SnackbarController,
    store: StoreState,
    coroutineScope: CoroutineScope
) {
    val creating = remember { mutableStateOf(false) }
    val canSubmit = remember { mutableStateOf(false) }
    val submitting = remember { mutableStateOf(false) }

    val emailFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()
    val confirmPasswordFocusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    if (!creating.value && !store.guest) {
        username = store.username
        email = store.email
    }

    val submit: () -> Unit = {
        keyboardController?.hide()
        coroutineScope.launch {
            handleSubmission(
                creating,
                submitting,
                canSubmit.value,
                username,
                email,
                password,
                confirmPassword,
                snackbarController,
                accountService,
                store.guest
            )

            username = if (store.guest) "" else store.username
            email = if (store.guest) "" else store.email
            password = ""
            confirmPassword = ""
            submitting.value = false
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        AnimatedVisibility(visible = creating.value) {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        canSubmit.value = canSubmitValues(
                            username,
                            email,
                            password,
                            confirmPassword,
                            store.guest
                        )
                    },
                    placeholder = { Text("Username") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        emailFocusRequester.requestFocus()
                    }),
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = "username")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        canSubmit.value = canSubmitValues(
                            username,
                            email,
                            password,
                            confirmPassword,
                            store.guest
                        )
                    },
                    placeholder = { Text("Email") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        passwordFocusRequester.requestFocus()
                    }),
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Email, contentDescription = "email")
                    },
                    modifier = Modifier
                        .focusRequester(emailFocusRequester)
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        canSubmit.value = canSubmitValues(
                            username,
                            email,
                            password,
                            confirmPassword,
                            store.guest
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        confirmPasswordFocusRequester.requestFocus()
                    }),
                    placeholder = { Text("Password") },
                    modifier = Modifier
                        .focusRequester(passwordFocusRequester)
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.VpnKey, contentDescription = "password")
                    }
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        canSubmit.value = canSubmitValues(
                            username,
                            email,
                            password,
                            confirmPassword,
                            store.guest
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(onGo = {
                        submit()
                    }),
                    placeholder = { Text("Confirm Password") },
                    modifier = Modifier
                        .focusRequester(confirmPasswordFocusRequester)
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.VpnKey,
                            contentDescription = "confirmPassword"
                        )
                    }
                )
            }
        }

        Button(
            onClick = submit,
            enabled = !creating.value || !submitting.value && canSubmit.value,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = if (store.guest) "Create account" else "Update account"
            )
        }
    }
}
