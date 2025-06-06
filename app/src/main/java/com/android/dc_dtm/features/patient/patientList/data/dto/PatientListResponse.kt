package com.android.dc_dtm.features.patient.patientList.data.dto

import java.util.*

data class PatientDto(
    val id: Int,
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
    val createdAt: Date,
    val exams: List<ExamDto>?
)

data class ExamDto(
    val id: Int,
    val patientId: Int,
    val userId: Int,
    val status: String,
    val result: String?,
    val observations: String?,
    val validated: Boolean?,
    val validatorId: Int?,
    val validatedAt: Date?,
    val createdAt: Date
)
