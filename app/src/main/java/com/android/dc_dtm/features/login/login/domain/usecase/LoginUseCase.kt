package features.login.domain.usecase

import features.login.domain.repository.AuthRepository
import features.login.domain.model.User

// Caso de uso: realizar login
class LoginUseCase(private val repository: AuthRepository) {

    suspend fun execute(username: String, password: String): Result<User> {
        return repository.login(username, password)
    }
}
