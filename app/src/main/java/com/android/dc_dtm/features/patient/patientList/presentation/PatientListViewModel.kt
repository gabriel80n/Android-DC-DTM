package com.android.dc_dtm.features.patient.patientList.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dc_dtm.features.patient.patientList.data.api.PatientApi
import com.android.dc_dtm.core.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientListViewModel(private val context: Context) : ViewModel() {

    private val patientApi: PatientApi = RetrofitInstance.getPatientApi(context)

    private val _state = MutableStateFlow(PatientListState())
    val state: StateFlow<PatientListState> = _state

    fun onEvent(intent: PatientListIntent) {
        when (intent) {
            is PatientListIntent.LoadPatients -> {
                loadPatients()
            }
            is PatientListIntent.SearchByName -> {
                searchPatients(intent.query)
            }
        }
    }

    private fun loadPatients() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val patients = patientApi.getPatients()
                _state.value = _state.value.copy(patients = patients, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.localizedMessage, isLoading = false)
            }
        }
    }

    private fun searchPatients(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, searchQuery = query)
            try {
                val patients = if (query.isBlank()) {
                    patientApi.getPatients()
                } else {
                    patientApi.searchPatientsByName(query)
                }
                _state.value = _state.value.copy(patients = patients, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.localizedMessage, isLoading = false)
            }
        }
    }
}
