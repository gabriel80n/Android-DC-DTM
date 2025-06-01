package com.android.dc_dtm.features.login.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.dc_dtm.R
import features.login.presentation.intent.LoginIntent
import androidx.compose.runtime.livedata.observeAsState
import features.login.presentation.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onLoginSuccess()
    }

    val snackbarMessageLiveData = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("snackbar")

    val snackbarMessageState = snackbarMessageLiveData?.observeAsState()
    val snackbarMessage = snackbarMessageState?.value


    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            // ✅ Dentro do escopo do LaunchedEffect, é permitido usar funções `suspend`
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            // ⚠️ Limpa a mensagem para não ser exibida novamente
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("snackbar")
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(R.string.login_logo_description),
            modifier = Modifier
                .height(240.dp)
                .padding(bottom = 48.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    stringResource(R.string.login_email_label),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    stringResource(R.string.login_password_label),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.login_forgot_password),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("confirm_email")
                }
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (state.isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else {
            Button(
                onClick = {
                    viewModel.handleIntent(LoginIntent.SubmitLogin(email, password))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(R.string.login_button_text))
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
