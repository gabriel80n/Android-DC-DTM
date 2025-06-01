package com.android.dc_dtm.core.api

import android.content.Context
import com.android.dc_dtm.core.helper.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {

    private val tokenManager = TokenManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getToken()
        val requestBuilder = chain.request().newBuilder()

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
