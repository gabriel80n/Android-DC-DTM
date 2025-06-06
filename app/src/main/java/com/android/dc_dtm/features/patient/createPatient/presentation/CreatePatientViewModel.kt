package com.android.dc_dtm.features.patient.createPatient.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dc_dtm.core.api.RetrofitInstance
import com.android.dc_dtm.features.patient.createPatient.data.dto.CreatePatientDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreatePatientViewModel(context: Context) : ViewModel() {

    private val _state = MutableStateFlow(CreatePatientViewState())
    val state: StateFlow<CreatePatientViewState> = _state

    private val api = RetrofitInstance.postCreatePatientApi(context)

    fun onEvent(intent: CreatePatientIntent) {
        when (intent) {
            is CreatePatientIntent.UpdateField -> {
                updateField(intent.field, intent.value)
            }

            is CreatePatientIntent.Submit -> {
                validateAndSubmit()
            }

            is CreatePatientIntent.Cancel -> {
                // handled by UI
            }
        }
    }

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


    private fun updateField(field: String, value: String) {
        val current = _state.value

        val updatedState = when (field) {
            "name" -> current.copy(name = value)
            "documentType" -> current.copy(documentType = value)
            "document" -> current.copy(document = value)
            "birthDate" -> {
                val parsedDate = try {
                    dateFormat.parse(value)
                } catch (e: Exception) {
                    null
                }
                current.copy(birthDate = parsedDate)
            }
            "phone" -> current.copy(phone = value)
            "address" -> current.copy(address = value)
            "gender" -> current.copy(gender = value)
            "maritalStatus" -> current.copy(maritalStatus = value)
            "race" -> current.copy(race = value)
            "educationLevel" -> current.copy(educationLevel = value)
            "origin" -> current.copy(origin = value)
            "annualIncome" -> current.copy(annualIncome = value)
            else -> current
        }

        _state.value = updatedState
    }


    private fun validateAndSubmit() {
        val state = _state.value

        val errors = mutableMapOf<String, String?>()

        if (state.name.isBlank()) errors["name"] = "Campo obrigatório"
        if (state.documentType.isBlank()) errors["documentType"] = "Campo obrigatório"
        if (state.document.isBlank()) errors["document"] = "Campo obrigatório"
        if (state.birthDate == null) errors["birthDate"] = "Campo obrigatório"
        if (state.phone.isBlank()) errors["phone"] = "Campo obrigatório"
        if (state.address.isBlank()) errors["address"] = "Campo obrigatório"
        if (state.gender.isBlank()) errors["gender"] = "Campo obrigatório"
        if (state.maritalStatus.isBlank()) errors["maritalStatus"] = "Campo obrigatório"
        if (state.race.isBlank()) errors["race"] = "Campo obrigatório"
        if (state.educationLevel.isBlank()) errors["educationLevel"] = "Campo obrigatório"
        if (state.origin.isBlank()) errors["origin"] = "Campo obrigatório"
        if (state.annualIncome.isBlank()) errors["annualIncome"] = "Campo obrigatório"
        else {
            val income = state.annualIncome.toDoubleOrNull()
            if (income == null || income < 0.0) {
                errors["annualIncome"] = "Informe uma renda anual válida (número positivo)."
            }
        }

        if (errors.isNotEmpty()) {
            _state.value = state.copy(fieldErrors = errors)
            return
        } else {
            // limpa erros
            _state.value = state.copy(fieldErrors = emptyMap(), error = null)
        }

        submitPatient()
    }

    private fun submitPatient() {
        val state = _state.value

        viewModelScope.launch {
            _state.value = state.copy(isLoading = true, error = null)

            try {
                val dto = CreatePatientDto(
                    name = state.name,
                    documentType = state.documentType,
                    document = state.document,
                    birthDate = state.birthDate ?: Date(),
                    phone = state.phone,
                    address = state.address,
                    gender = state.gender,
                    maritalStatus = state.maritalStatus,
                    race = state.race,
                    educationLevel = state.educationLevel,
                    origin = state.origin,
                    annualIncome = state.annualIncome.toDoubleOrNull() ?: 0.0
                )

                val response = api.createPatient(dto)
                if (response.isSuccessful) {
                    _state.value = state.copy(isLoading = false, success = true)
                } else {
                    _state.value = state.copy(
                        isLoading = false,
                        error = response.errorBody()?.string() ?: "Erro ao criar paciente."
                    )
                }

            } catch (e: Exception) {
                _state.value = state.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Erro inesperado."
                )
            }
        }
    }
}
