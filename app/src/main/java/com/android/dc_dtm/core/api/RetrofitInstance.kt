package com.android.dc_dtm.core.api

import android.content.Context
import com.android.dc_dtm.features.login.confirm_email.data.api.ConfirmEmailApi
import com.android.dc_dtm.features.login.forgot_password.data.api.ResetPasswordApi
import com.android.dc_dtm.features.login.login.data.api.LoginApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private fun getRetrofit(context: Context): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = AuthInterceptor(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getLoginApi(context: Context): LoginApi =
        getRetrofit(context).create(LoginApi::class.java)

    fun getConfirmEmailApi(context: Context): ConfirmEmailApi =
        getRetrofit(context).create(ConfirmEmailApi::class.java)

    fun postNewPassword(context: Context): ResetPasswordApi =
        getRetrofit(context).create(ResetPasswordApi::class.java)
    // você pode continuar criando outros assim:
    // fun getUserApi(context: Context): UserApi = ...
}

