package com.android.dc_dtm.features.patient.patientDetails.presentation

import java.util.Date

sealed class PatientDetailIntent {

    data class LoadPatient(val id: Int) : PatientDetailIntent()
    data object ToggleEdit : PatientDetailIntent()
    data class UpdateField(val field: String, val value: String) : PatientDetailIntent()
    data object SavePatient : PatientDetailIntent()
    data class UpdateDateField(val field: String, val date: Date) : PatientDetailIntent()
    data class DeletePatient(val patientId: Int) : PatientDetailIntent()
}

