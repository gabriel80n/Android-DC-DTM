package com.android.dc_dtm.features.patient.patientList.presentation

import com.android.dc_dtm.features.patient.patientList.data.dto.PatientDto

data class PatientListState(
    val patients: List<PatientDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)
