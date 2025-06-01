package com.android.dc_dtm.features.login.confirm_email.presentation

import androidx.activity.compose.LocalActivityResultRegistryOwner.current
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dc_dtm.core.presentation.SharedAuthViewModel
import com.android.dc_dtm.features.login.confirm_email.data.api.ConfirmEmailApi
import com.android.dc_dtm.features.login.confirm_email.data.dto.ConfirmEmailRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class ConfirmEmailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class ConfirmEmailViewModel(
    private val confirmEmailApi: ConfirmEmailApi, // ← Injete o LoginApi aqui
    private val sharedAuthViewModel: SharedAuthViewModel
) : ViewModel() {

    private val _state = MutableStateFlow(ConfirmEmailViewState())
    val state: StateFlow<ConfirmEmailViewState> = _state

    fun onEvent(intent: ConfirmEmailIntent) {
        when (intent) {
            is ConfirmEmailIntent.SendCode -> {
                _state.value = ConfirmEmailViewState(isLoading = true)

                viewModelScope.launch {
                    try {
                        val current = _state.value
                        val response = confirmEmailApi.sendPasswordRecoveryCode(
                            ConfirmEmailRequest(email = intent.email)
                        )

                        sharedAuthViewModel.setEmail(intent.email) // ← salva aqui
                        _state.value = current.copy(isLoading = false,
                            isSuccess = true,
                            successMessage = "Email enviado!")


                    } catch (e: IOException) {
                        _state.value = ConfirmEmailViewState(error = "Erro de conexão, tente mais tarde")
                    } catch (e: HttpException) {
                        _state.value = ConfirmEmailViewState(error = "Erro inesperado, tente mais tarde")
                    }
                }
            }

            else -> {}
        }
    }
}
