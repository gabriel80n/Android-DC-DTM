package com.android.dc_dtm.features.login.login.data.api

import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("login")
    suspend fun login(@Body loginRequest: com.android.dc_dtm.features.login.login.data.api.LoginRequest): com.android.dc_dtm.features.login.login.data.api.LoginResponse
}
