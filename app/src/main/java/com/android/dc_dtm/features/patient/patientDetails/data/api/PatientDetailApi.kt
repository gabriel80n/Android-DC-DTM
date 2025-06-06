package com.android.dc_dtm.features.patient.patientDetails.data.api

import com.android.dc_dtm.features.patient.patientDetails.data.dto.PatientDetailsDto
import com.android.dc_dtm.features.patient.patientDetails.data.dto.UpdatePatientDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PatientDetailApi {
    @GET("patients/{id}")
    suspend fun getPatientById(@Path("id") id: Int): Response<PatientDetailsDto>

    @PUT("patients/{id}")
    suspend fun updatePatient(
        @Path("id") id: Int,
        @Body patient: UpdatePatientDto
    ): Response<PatientDetailsDto>

    @DELETE("patients/{id}")
    suspend fun deletePatient(@Path("id") id: Int): Response<Unit>
}
