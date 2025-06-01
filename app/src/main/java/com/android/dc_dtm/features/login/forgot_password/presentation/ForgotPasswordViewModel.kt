package com.android.dc_dtm.features.login.forgot_password.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dc_dtm.core.presentation.SharedAuthViewModel
import com.android.dc_dtm.features.login.confirm_email.data.api.ConfirmEmailApi
import com.android.dc_dtm.features.login.confirm_email.data.dto.ConfirmEmailRequest
import com.android.dc_dtm.features.login.forgot_password.data.api.ResetPasswordApi
import com.android.dc_dtm.features.login.forgot_password.data.dto.ResetPasswordRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class ForgotPasswordViewModel(
    private val api: ResetPasswordApi,
    private val confirmEmailApi: ConfirmEmailApi,
    private val sharedAuthViewModel: SharedAuthViewModel,
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state: StateFlow<ForgotPasswordUiState> = _state

    fun onEvent(event: ForgotPasswordIntent) {
        when (event) {
            is ForgotPasswordIntent.CodeChanged -> {
                _state.value = _state.value.copy(code = event.value)
            }
            is ForgotPasswordIntent.NewPasswordChanged -> {
                _state.value = _state.value.copy(newPassword = event.value)
            }
            is ForgotPasswordIntent.ConfirmPasswordChanged -> {
                _state.value = _state.value.copy(confirmPassword = event.value)
            }
            is ForgotPasswordIntent.Submit -> {
                submitNewPassword()
            }
            is ForgotPasswordIntent.SendCode -> {
                sendCode()
            }
        }
    }

    private fun submitNewPassword() {
        viewModelScope.launch {
            val current = _state.value
            if (current.newPassword != current.confirmPassword) {
                _state.value = current.copy(error = "As senhas não coincidem")
                return@launch
            }

            val email = sharedAuthViewModel.email.value
            if (email.isBlank()) {
                _state.value = current.copy(error = "Erro inesperado, tente mais tarde")
                return@launch
            }

            _state.value = current.copy(isLoading = true, error = null)

            try {
                val response = api.resetPassword(
                    ResetPasswordRequest(
                        email = email,
                        code = current.code,
                        newPassword = current.newPassword
                    )
                )

                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Senha alterada com sucesso"
                    _state.value = current.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = message,
                        error= null,
                    )
                } else {
                    val errorMessage = try {
                        val errorJson = response.errorBody()?.string()
                        if (!errorJson.isNullOrEmpty()) {
                            val json = JSONObject(errorJson)

                            val message = json.get("message")
                            when (message) {
                                is String -> message
                                is JSONArray -> {
                                    if (message.length() > 0) message.getString(0)
                                    else "Erro desconhecido. Tente novamente mais tarde."
                                }
                                else -> "Erro desconhecido. Tente novamente mais tarde."
                            }
                        } else {
                            "Erro desconhecido. Tente novamente mais tarde."
                        }
                    } catch (e: Exception) {
                        "Erro ao processar a resposta do servidor."
                    }

                    _state.value = current.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }


            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = "Erro inesperado, tente mais tarde")
            }
        }
    }

    private fun sendCode() {
        viewModelScope.launch {
            val email = sharedAuthViewModel.email.value

            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val response = confirmEmailApi.sendPasswordRecoveryCode(
                    ConfirmEmailRequest(email = email)
                )

                if (response.isSuccessful) {
                    // Código reenviado com sucesso
                    _state.value = _state.value.copy(
                        isLoading = false,
                        successMessage = "Código reenviado com sucesso!"
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Erro ao reenviar código: ${response.code()}"
                    )
                }
            } catch (e: IOException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Erro de conexão."
                )
            } catch (e: HttpException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Erro inesperado: ${e.message}"
                )
            }
        }
    }

}
