package com.android.dc_dtm.features.dashboard.data.dto

data class DashboardResponse (
    val totalPacients: Int,
    val totalDiagnostics: Int,
    val pendingDiagnostics: Int,
    val validatedDiagnostics: Int,
    val totalUsers: Int,
)