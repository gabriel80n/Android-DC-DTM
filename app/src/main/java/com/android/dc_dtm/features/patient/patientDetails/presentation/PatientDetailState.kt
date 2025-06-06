package com.android.dc_dtm.features.patient.patientDetails.presentation

import com.android.dc_dtm.features.patient.patientDetails.data.dto.PatientDetailsDto
import java.util.Date

data class PatientDetailState(
    val patient: PatientDetailsDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditable: Boolean = false,

    // Campos do tipo correto
    val name: String = "",
    val document: String = "",
    val documentType: String = "",
    val birthDate: Date? = null,
    val phone: String = "",
    val address: String = "",
    val gender: String = "",
    val race: String = "",
    val maritalStatus: String = "",
    val educationLevel: String = "",
    val origin: String = "",
    val annualIncome: Double = 0.0,
    val createdAt: Date? = null,
)

