package com.android.dc_dtm.features.dashboard.presentation

data class DashboardViewState (
    val totalPacients: Int = 0,
    val totalDiagnostics: Int = 0,
    val pendingDiagnostics: Int = 0,
    val validatedDiagnostics: Int = 0,
    val totalUsers: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null

)