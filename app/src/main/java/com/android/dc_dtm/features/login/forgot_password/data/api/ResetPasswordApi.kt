package com.android.dc_dtm.features.login.forgot_password.data.api

import com.android.dc_dtm.features.login.forgot_password.data.dto.ResetPasswordRequest
import com.android.dc_dtm.features.login.forgot_password.data.dto.ResetPasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ResetPasswordApi {
    @POST("reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>
}
