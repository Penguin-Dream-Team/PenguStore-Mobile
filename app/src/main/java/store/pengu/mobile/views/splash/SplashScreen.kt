package store.pengu.mobile.views.splash

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.outlinedButtonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import kotlinx.coroutines.launch
import store.pengu.mobile.R
import store.pengu.mobile.errors.PenguStoreApiException
import store.pengu.mobile.services.AccountService

@ExperimentalComposeUiApi
@Composable
fun SplashScreen(
    navController: NavController,
    accountService: AccountService,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var canRegister by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(top = 75.dp)) {
            Surface(
                modifier = Modifier
                    .height(128.dp)
                    .width(128.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painterResource(R.drawable.pengulogo),
                    contentDescription = "PenguShop",
                    contentScale = ContentScale.Crop,
                )
            }

            val logoFont = MaterialTheme.typography.h6
            Text(
                text = "PenguStore", fontSize = logoFont.fontSize,
                fontWeight = logoFont.fontWeight,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .fillMaxWidth()
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current

            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var canLogin by remember { mutableStateOf(false) }

            val submit: () -> Unit = {
                keyboardController?.hide()
                canRegister = false
                coroutineScope.launch {
                    try {
                        username = accountService.login(username, password)
                        Toast.makeText(context, "Welcome $username", Toast.LENGTH_SHORT).show()
                        navController.navigate("dashboard")
                    } catch (e: PenguStoreApiException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        canRegister = true
                    }
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
                onClick = submit, enabled = canLogin && canRegister,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 75.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Login")
            }
        }

        Button(
            onClick = {
                canRegister = false
                coroutineScope.launch {
                    try {
                        val username = accountService.registerGuest()
                        Toast.makeText(context, "Welcome $username", Toast.LENGTH_SHORT).show()
                        navController.navigate("dashboard")
                    } catch (e: PenguStoreApiException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        canRegister = true
                    }
                }
            },
            contentPadding = PaddingValues(16.dp),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp),
            colors = outlinedButtonColors(backgroundColor = Color.Transparent),
            modifier = Modifier.padding(bottom = 16.dp),
            enabled = canRegister
        ) {
            Text(
                text = "Continue without account",
                textDecoration = TextDecoration.Underline
            )
        }
    }
}
