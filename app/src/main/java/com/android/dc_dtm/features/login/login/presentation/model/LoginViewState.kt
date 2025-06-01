package features.login.presentation.model

// Estado da tela: carregando, sucesso ou erro
data class LoginViewState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
