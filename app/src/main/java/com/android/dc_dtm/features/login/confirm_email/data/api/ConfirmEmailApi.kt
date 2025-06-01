package com.android.dc_dtm.features.login.confirm_email.data.api

import com.android.dc_dtm.features.login.confirm_email.data.dto.ConfirmEmailRequest
import com.android.dc_dtm.features.login.confirm_email.data.dto.ConfirmEmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ConfirmEmailApi {
    @POST("forgot-password")
    suspend fun sendPasswordRecoveryCode(@Body request: ConfirmEmailRequest): Response<ConfirmEmailResponse>
}
