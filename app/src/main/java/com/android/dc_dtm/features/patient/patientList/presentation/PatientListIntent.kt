package com.android.dc_dtm.features.patient.patientList.presentation

sealed class PatientListIntent {
    object LoadPatients : PatientListIntent()
    data class SearchByName(val query: String) : PatientListIntent()
}
