package com.android.dc_dtm.features.patient.patientDetails.data.dto

import com.android.dc_dtm.features.patient.patientList.data.dto.ExamDto
import java.util.Date

data class PatientDetailsDto(
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
