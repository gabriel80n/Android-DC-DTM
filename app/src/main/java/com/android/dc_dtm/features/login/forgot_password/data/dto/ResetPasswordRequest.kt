package com.android.dc_dtm.features.login.forgot_password.data.dto

data class ResetPasswordRequest (val email: String, val code: String, val newPassword: String)