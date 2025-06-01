package com.android.dc_dtm.features.login.forgot_password.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.dc_dtm.R
import com.android.dc_dtm.core.api.RetrofitInstance
import com.android.dc_dtm.core.presentation.SharedAuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    sharedAuthViewModel: SharedAuthViewModel,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val confirmEmailApi = RetrofitInstance.getConfirmEmailApi(context)
    val resetPasswordApi = RetrofitInstance.postNewPassword(context)
    val viewModel = remember {
        ForgotPasswordViewModel(resetPasswordApi, confirmEmailApi, sharedAuthViewModel)
    }
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let { message ->
            // Exibe o snackbar primeiro


            // Navega logo em seguida (sem esperar o snackbar desaparecer)
            if (state.isSuccess) {
                navController.navigate("login") {
                    popUpTo("forgot_password") { inclusive = true }
                }
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("snackbar", "Senha alterada com sucesso.")
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.forgot_password_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.forgot_password_subtitle),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.code,
            onValueChange = { viewModel.onEvent(ForgotPasswordIntent.CodeChanged(it)) },
            label = {
                Text(
                    stringResource(R.string.recovery_code_label),
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
            value = state.newPassword,
            onValueChange = { viewModel.onEvent(ForgotPasswordIntent.NewPasswordChanged(it)) },
            label = {
                Text(
                    stringResource(R.string.new_password_label),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.onEvent(ForgotPasswordIntent.ConfirmPasswordChanged(it)) },
            label = {
                Text(
                    stringResource(R.string.confirm_password_label),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        state.error?.let {

            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    when {
                        state.code.isBlank() -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Por favor, preencha o código de recuperação.")
                            }
                        }
                        state.newPassword.isBlank() -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Por favor, preencha a nova senha.")
                            }
                        }
                        state.confirmPassword.isBlank() -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Por favor, confirme a senha.")
                            }
                        }
                        else -> {
                            viewModel.onEvent(ForgotPasswordIntent.Submit)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.confirm_new_password_button))
            }

            OutlinedButton(
                onClick = { viewModel.onEvent(ForgotPasswordIntent.SendCode) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.resend_code_button))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.back_to_login),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.navigate("login") {
                            popUpTo("forgot_password") { inclusive = true }
                        }
                    },
                color = MaterialTheme.colorScheme.primary
            )
        }


    }
}
