package features.login.presentation.intent

// Ações que o usuário pode executar na tela de login
sealed class LoginIntent {
    data class SubmitLogin(val username: String, val password: String) : LoginIntent()
}
