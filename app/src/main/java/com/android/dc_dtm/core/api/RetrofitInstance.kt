package com.android.dc_dtm.core.api

import android.content.Context
import com.android.dc_dtm.features.dashboard.data.api.DashboardApi
import com.android.dc_dtm.features.login.confirm_email.data.api.ConfirmEmailApi
import com.android.dc_dtm.features.login.forgot_password.data.api.ResetPasswordApi
import com.android.dc_dtm.features.login.login.data.api.LoginApi
import com.android.dc_dtm.features.patient.createPatient.data.api.CreatePatientApi
import com.android.dc_dtm.features.patient.patientDetails.data.api.PatientDetailApi
import com.android.dc_dtm.features.patient.patientList.data.api.PatientApi
import com.google.gson.GsonBuilder
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

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)) // use o Gson personalizado
            .build()
    }

    fun getLoginApi(context: Context): LoginApi =
        getRetrofit(context).create(LoginApi::class.java)

    fun getConfirmEmailApi(context: Context): ConfirmEmailApi =
        getRetrofit(context).create(ConfirmEmailApi::class.java)

    fun postNewPassword(context: Context): ResetPasswordApi =
        getRetrofit(context).create(ResetPasswordApi::class.java)

    fun getDashboardApi(context: Context): DashboardApi =
        getRetrofit(context).create(DashboardApi::class.java)

    fun getPatientApi(context: Context): PatientApi =
        getRetrofit(context).create(PatientApi::class.java)

    fun getPatientDetailApi(context: Context): PatientDetailApi =
        getRetrofit(context).create(PatientDetailApi::class.java)

    fun postCreatePatientApi(context: Context): CreatePatientApi =
        getRetrofit(context).create(CreatePatientApi::class.java)

    // você pode continuar criando outros assim:
    // fun getUserApi(context: Context): UserApi = ...
}

