package com.android.dc_dtm.features.login.confirm_email.presentation

import android.util.Patterns

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.dc_dtm.R
import com.android.dc_dtm.core.api.RetrofitInstance
import com.android.dc_dtm.core.presentation.SharedAuthViewModel
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@Composable
fun ConfirmEmailScreen(
    navController: NavController,
    sharedAuthViewModel: SharedAuthViewModel,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current

    // 🔧 Obtemos a instância da API usando RetrofitInstance
    val api = remember { RetrofitInstance.getConfirmEmailApi(context) }

    // 🧠 Criamos o ViewModel com a dependência da API
    val viewModel = remember { ConfirmEmailViewModel(api, sharedAuthViewModel) }

    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }

    // Navega para a próxima tela se for sucesso
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate("forgot_password") {
                popUpTo("confirm_email") { inclusive = true }
            }
        }
    }
    val coroutineScope = rememberCoroutineScope()


    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.forgot_password_confirm_email_title),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.forgot_password_confirm_email_desc),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    stringResource(R.string.forgot_password_confirm_email_input_label),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            LocalContext.current // já deve estar declarado lá em cima

            Button(
                onClick = {
                    when {
                        email.isBlank() -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.type_an_email) // ← AQUI!
                                )
                            }
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.type_valid_email) // ← AQUI!
                                )
                            }
                        }
                        else -> {
                            viewModel.onEvent(ConfirmEmailIntent.SendCode(email))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.forgot_password_confirm_email_send_button))
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.forgot_password_confirm_email_back_to_login),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navController.navigate("login") {
                    popUpTo("confirm_email") { inclusive = true }
                }
            }
        )

        state.error?.let { errorMsg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
