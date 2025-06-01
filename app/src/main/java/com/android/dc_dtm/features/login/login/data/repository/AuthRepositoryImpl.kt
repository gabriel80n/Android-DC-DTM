package com.android.dc_dtm.features.login.login.data.repository

import android.content.Context
import com.android.dc_dtm.core.helper.TokenManager
import features.login.domain.model.User
import features.login.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: com.android.dc_dtm.features.login.login.data.api.LoginApi,
    context: Context
) : AuthRepository {

    private val tokenManager = TokenManager(context)

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val response = api.login(
                com.android.dc_dtm.features.login.login.data.api.LoginRequest(
                    username,
                    password
                )
            )

            // Salvar token ao fazer login
            tokenManager.saveToken(response.accessToken)

            Result.success(User(response.id, response.name, response.email))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
