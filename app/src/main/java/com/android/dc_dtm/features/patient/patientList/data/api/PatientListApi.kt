package com.android.dc_dtm.features.patient.patientList.data.api

import com.android.dc_dtm.features.patient.patientList.data.dto.PatientDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PatientApi {
    @GET("patients")
    suspend fun getPatients(): List<PatientDto>

    @GET("patients/search")
    suspend fun searchPatientsByName(
        @Query("name") name: String
    ): List<PatientDto>
}
