package com.android.dc_dtm.features.patient.createPatient.data.api;


import com.android.dc_dtm.features.patient.createPatient.data.dto.CreatePatientDto;

import kotlin.Unit;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface CreatePatientApi {
    @POST("patients")
    suspend fun createPatient(@Body patient:CreatePatientDto): Response<Unit>
}
