package com.android.dc_dtm.features.patient.patientDetails.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dc_dtm.core.api.RetrofitInstance
import com.android.dc_dtm.features.patient.patientDetails.data.dto.UpdatePatientDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PatientDetailViewModel(
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(PatientDetailState())
    val state: StateFlow<PatientDetailState> = _state

    private val api = RetrofitInstance.getPatientDetailApi(context)

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun onEvent(intent: PatientDetailIntent, onSuccess: (() -> Unit)? = null) {
        when (intent) {
            is PatientDetailIntent.LoadPatient -> loadPatient(intent.id)
            is PatientDetailIntent.ToggleEdit -> toggleEdit()
            is PatientDetailIntent.UpdateField -> updateField(intent.field, intent.value)
            is PatientDetailIntent.SavePatient -> savePatient()
            is PatientDetailIntent.UpdateDateField -> updateDateField(intent.field, intent.date)
            is PatientDetailIntent.DeletePatient -> deletePatient(intent.patientId, onSuccess)
        }
    }
    private fun updateDateField(field: String, date: Date) {
        val current = _state.value
        when (field) {
            "birthDate" -> _state.value = current.copy(birthDate = date)
            "createdAt" -> _state.value = current.copy(createdAt = date)
        }
    }
    private fun toggleEdit() {
        val currentEditable = _state.value.isEditable
        if (!currentEditable) {
            // Entrando em modo edição: popular campos editáveis com dados do paciente atual
            val patient = _state.value.patient
            patient?.let {
                _state.value = _state.value.copy(
                    isEditable = true,
                    name = it.name,
                    document = it.document,
                    documentType = it.documentType,
                    birthDate = it.birthDate,
                    phone = it.phone,
                    address = it.address,
                    gender = it.gender,
                    race = it.race,
                    maritalStatus = it.maritalStatus,
                    educationLevel = it.educationLevel,
                    origin = it.origin,
                    annualIncome = it.annualIncome,
                    createdAt = it.createdAt
                )
            }
        } else {
            // Saindo do modo edição (cancelar)
            _state.value = _state.value.copy(isEditable = false)
        }
    }

    fun deletePatient(patientId: Int, onSuccess: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val response = api.deletePatient(patientId)
                if (response.isSuccessful) {
                    _state.update { it.copy(patient = null) }
                    onSuccess?.invoke()
                } else {
                    _state.update { it.copy(error = "Erro ao deletar paciente") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage ?: "Erro inesperado") }
            }
        }
    }


    private fun updateField(field: String, value: String) {
        val current = _state.value
        when (field.lowercase()) {
            "name" -> _state.value = current.copy(name = value)
            "document" -> _state.value = current.copy(document = value)
            "documenttype" -> _state.value = current.copy(documentType = value)
            "birthdate" -> _state.value = current.copy(birthDate = parseDate(value))
            "phone" -> _state.value = current.copy(phone = value)
            "address" -> _state.value = current.copy(address = value)
            "gender" -> _state.value = current.copy(gender = value)
            "race" -> _state.value = current.copy(race = value)
            "maritalstatus" -> _state.value = current.copy(maritalStatus = value)
            "educationlevel" -> _state.value = current.copy(educationLevel = value)
            "origin" -> _state.value = current.copy(origin = value)
            "annualincome" -> {
                val doubleValue = value.toDoubleOrNull() ?: 0.0
                _state.value = current.copy(annualIncome = doubleValue)
            }
            "createdat" -> _state.value = current.copy(createdAt = parseDate(value))
        }
    }


    private fun savePatient() {
        viewModelScope.launch {
            val state = _state.value
            val patientId = state.patient?.id ?: return@launch

            val body = UpdatePatientDto(
                name = state.name,
                document = state.document,
                documentType = state.documentType,
                birthDate = state.birthDate ?: return@launch,
                phone = state.phone,
                address = state.address,
                gender = state.gender,
                race = state.race,
                maritalStatus = state.maritalStatus,
                educationLevel = state.educationLevel,
                origin = state.origin,
                annualIncome = state.annualIncome,
                createdAt = state.createdAt ?: return@launch
            )
            Log.d("DEBUG", "birthDate: ${state.birthDate}, type: ${state.birthDate?.javaClass}")
            Log.d("DEBUG", "createdAt: ${state.createdAt}, type: ${state.createdAt?.javaClass}")

            val response = api.updatePatient(patientId, body)
            if (response.isSuccessful) {
                _state.value = _state.value.copy(
                    patient = response.body(),
                    isEditable = false,
                    error = null
                )
            } else {
                _state.value = _state.value.copy(error = "Erro ao atualizar paciente")
            }
        }
    }



    private fun loadPatient(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val response = api.getPatientById(id)
                if (response.isSuccessful) {
                    val patient = response.body()
                    patient?.let {
                        _state.value = _state.value.copy(
                            patient = it,
                            isLoading = false,
                            name = it.name,
                            document = it.document,
                            documentType = it.documentType,
                            birthDate = it.birthDate,
                            phone = it.phone,
                            address = it.address,
                            gender = it.gender,
                            race = it.race,
                            maritalStatus = it.maritalStatus,
                            educationLevel = it.educationLevel,
                            origin = it.origin,
                            annualIncome = it.annualIncome,
                            createdAt = it.createdAt
                        )
                    }
                } else {
                    _state.value = _state.value.copy(
                        error = "Erro ao carregar paciente",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Erro: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private fun parseDate(dateString: String?): Date? {
        return try {
            dateString?.let { dateFormat.parse(it) }
        } catch (e: Exception) {
            null
        }
    }
    }