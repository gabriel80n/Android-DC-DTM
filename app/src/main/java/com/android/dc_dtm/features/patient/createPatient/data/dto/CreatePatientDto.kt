package com.android.dc_dtm.features.patient.createPatient.data.dto

import java.util.Date


data class CreatePatientDto(
    val name: String,
    val documentType: String,
    val document: String,
    val birthDate: Date,
    val phone: String,
    val address: String,
    val gender: String,
    val maritalStatus: String,
    val race: String,
    val educationLevel: String,
    val origin: String,
    val annualIncome: Double
)
