package features.login.domain.repository

import features.login.domain.model.User

// Contrato para autenticação
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}
