package com.android.dc_dtm.features.patient.patientDetails.data.dto

import java.util.Date

data class UpdatePatientDto(
    val name: String,
    val document: String,
    val documentType: String,
    val birthDate: Date,
    val phone: String,
    val address: String,
    val gender: String,
    val race: String,
    val maritalStatus: String,
    val educationLevel: String,
    val origin: String,
    val annualIncome: Double,
    val createdAt: Date
)
