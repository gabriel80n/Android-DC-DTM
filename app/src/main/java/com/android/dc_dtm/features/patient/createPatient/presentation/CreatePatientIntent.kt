package com.android.dc_dtm.features.patient.createPatient.presentation


sealed class CreatePatientIntent {
    data class UpdateField(val field: String, val value: String) : CreatePatientIntent()
    object Submit : CreatePatientIntent()
    object Cancel : CreatePatientIntent()
}
