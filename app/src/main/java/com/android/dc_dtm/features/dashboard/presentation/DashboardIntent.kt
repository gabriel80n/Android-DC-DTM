package com.android.dc_dtm.features.dashboard.presentation


sealed class DashboardIntent {
    object GetStats : DashboardIntent()
}