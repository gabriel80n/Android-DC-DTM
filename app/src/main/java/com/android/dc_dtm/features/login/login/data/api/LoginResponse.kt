package com.android.dc_dtm.features.login.login.data.api

import com.google.gson.annotations.SerializedName

data class LoginResponse(val id: String, val name: String, val email: String, @SerializedName("access_token") val accessToken: String)