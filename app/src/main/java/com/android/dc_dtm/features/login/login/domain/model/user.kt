package features.login.domain.model

// Entidade de domínio representando usuário
data class User(
    val id: String,
    val name: String,
    val email: String,
)
