package com.android.dc_dtm.features.login.confirm_email.presentation


sealed class ConfirmEmailIntent {
    data class SendCode(val email: String) : ConfirmEmailIntent()
}
