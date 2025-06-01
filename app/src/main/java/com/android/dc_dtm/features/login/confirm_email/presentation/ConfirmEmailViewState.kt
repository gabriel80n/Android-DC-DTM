package com.android.dc_dtm.features.login.confirm_email.presentation

data class ConfirmEmailViewState (
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
    )