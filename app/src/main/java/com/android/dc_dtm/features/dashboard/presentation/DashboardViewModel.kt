package com.android.dc_dtm.features.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dc_dtm.features.dashboard.data.api.DashboardApi
import com.android.dc_dtm.features.login.confirm_email.data.dto.ConfirmEmailRequest
import com.android.dc_dtm.features.login.confirm_email.presentation.ConfirmEmailViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DashboardViewModel(
    private val dashboardApi: DashboardApi
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardViewState())
    val state: StateFlow<DashboardViewState> = _state

    fun onEvent(intent: DashboardIntent){
        when(intent) {
            is DashboardIntent.GetStats -> {
                _state.value = DashboardViewState(isLoading = true)
                viewModelScope.launch {
                    try {
                        val current = _state.value
                        val response = dashboardApi.getStats()

                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                _state.value = DashboardViewState(
                                    isLoading = false,
                                    isSuccess = true,
                                    totalPacients = body.totalPacients,
                                    totalDiagnostics = body.totalDiagnostics,
                                    pendingDiagnostics = body.pendingDiagnostics,
                                    validatedDiagnostics = body.validatedDiagnostics,
                                    totalUsers = body.totalUsers
                                )
                            } else {
                                _state.value = DashboardViewState(error = "Resposta vazia do servidor.")
                            }
                        } else {
                            _state.value = DashboardViewState(error = "Erro ao buscar dados: ${response.code()}")
                        }


                    } catch (e: IOException) {
                        _state.value = DashboardViewState(error = "Erro de conexão, tente mais tarde")
                    } catch (e: HttpException) {
                        _state.value = DashboardViewState(error = "Erro inesperado, tente mais tarde")
                    }
                }
            }
        }
    }


}