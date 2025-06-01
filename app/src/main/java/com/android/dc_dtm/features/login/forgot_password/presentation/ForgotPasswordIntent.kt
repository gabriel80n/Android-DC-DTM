package com.android.dc_dtm.features.login.forgot_password.presentation

sealed class ForgotPasswordIntent {
    data class CodeChanged(val value: String) : ForgotPasswordIntent()
    data class NewPasswordChanged(val value: String) : ForgotPasswordIntent()
    data class ConfirmPasswordChanged(val value: String) : ForgotPasswordIntent()
    data object Submit : ForgotPasswordIntent()
    data object SendCode : ForgotPasswordIntent()
}