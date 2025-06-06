package com.android.dc_dtm.features.patient.createPatient.presentation

import java.util.Date


data class CreatePatientViewState(
    val name: String = "",
    val documentType: String = "CPF",
    val document: String = "",
    val birthDate: Date? = null,
    val phone: String = "",
    val address: String = "",
    val gender: String = "Masculino",
    val maritalStatus: String = "Solteiro",
    val race: String = "",
    val educationLevel: String = "",
    val origin: String = "",
    val annualIncome: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val fieldErrors: Map<String, String?> = emptyMap()
)
