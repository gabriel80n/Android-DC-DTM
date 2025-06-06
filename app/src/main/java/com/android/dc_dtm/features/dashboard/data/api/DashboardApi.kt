package com.android.dc_dtm.features.dashboard.data.api

import com.android.dc_dtm.features.dashboard.data.dto.DashboardResponse
import com.android.dc_dtm.features.login.confirm_email.data.dto.ConfirmEmailRequest
import com.android.dc_dtm.features.login.confirm_email.data.dto.ConfirmEmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DashboardApi {
    @GET("dashboard")
    suspend fun getStats(): Response<DashboardResponse>
}