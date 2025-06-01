package features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.login.domain.usecase.LoginUseCase
import features.login.presentation.intent.LoginIntent
import features.login.presentation.model.LoginViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState> = _state

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.SubmitLogin -> login(intent.username, intent.password)
        }
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginViewState(isLoading = true)

            val result = loginUseCase.execute(username, password)

            _state.value = if (result.isSuccess) {
                LoginViewState(isSuccess = true)
            } else {
                LoginViewState(error = result.exceptionOrNull()?.message)
            }
        }
    }
}
