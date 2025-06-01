package com.android.dc_dtm.features.login.forgot_password.presentation

data class ForgotPasswordUiState(
    val code: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)
