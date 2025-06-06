package com.android.dc_dtm.features.patient.patientDetails.presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.isEditable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.dc_dtm.core.api.RetrofitInstance
import com.android.dc_dtm.core.helper.TokenManager
import com.android.dc_dtm.core.presentation.components.AppScaffoldWithDrawer
import com.android.dc_dtm.core.presentation.components.SimpleScaffold
import com.android.dc_dtm.core.presentation.components.SimpleTopBar
import com.android.dc_dtm.features.dashboard.presentation.DashboardViewModel
import com.android.dc_dtm.features.patient.patientDetails.data.dto.PatientDetailsDto
import com.android.dc_dtm.features.patient.patientList.data.dto.PatientDto
import com.android.dc_dtm.ui.theme.LightGrayTopBar
import java.text.SimpleDateFormat
import java.util.*

fun examStatusFormat(status: String): String {
    return when (status) {
        "completed" -> "Finalizado"
        "pending_validation" -> "Pendente de Validação"
        else -> status.replace("_", " ").replaceFirstChar { it.uppercaseChar() }
    }
}


@Composable
fun PatientDetailScreen(
    navController: NavController,
    patientId: Int,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { PatientDetailViewModel(context) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(patientId) {
        viewModel.onEvent(PatientDetailIntent.LoadPatient(patientId))
    }

    SimpleScaffold(
        title = "Detalhes do Paciente",
        onBackClick = onBackClick
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    state.error != null -> {
                        Text("Erro: ${state.error}", color = MaterialTheme.colorScheme.error)
                    }

                    state.patient != null -> {
                        val patient = state.patient!!
                        PatientForm(
                            state = state,
                            onFieldChange = { field, value -> viewModel.onEvent(PatientDetailIntent.UpdateField(field, value)) },
                            onEditToggle = { viewModel.onEvent(PatientDetailIntent.ToggleEdit) },
                            onSave = { viewModel.onEvent(PatientDetailIntent.SavePatient) },
                            viewModel = viewModel,
                            patientId,
                            navController
                        )

                    }
                }
            }
        }
    }
}


// Função para formatar telefone (55) 55555-5555
fun formatPhone(input: String): String {
    val digits = input.filter { it.isDigit() }
    return if (digits.length >= 11) {
        "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7, 11)}"
    } else {
        input
    }
}

@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selected: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, color = Color.White)
        Box {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled) { expanded = true }, // permite clique mesmo com readOnly
                enabled = enabled,
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray,
                    unfocusedBorderColor = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray,
                    disabledTextColor = Color.White,
                    disabledLabelColor = Color.White,
                    disabledBorderColor = Color.Gray
                ),
                trailingIcon = {
                    IconButton(onClick = { if (enabled) expanded = !expanded }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color.White)
                    }
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}




@Composable
fun PatientForm(
    state: PatientDetailState,
    onFieldChange: (String, String) -> Unit,
    onEditToggle: () -> Unit,
    onSave: () -> Unit,
    viewModel: PatientDetailViewModel,
    patientId: Int,
    navController: NavController
) {
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val exam = state.patient?.exams?.firstOrNull()
    val status = exam?.status?.let { examStatusFormat(it) } ?: "Nenhum exame realizado"
    val diagnosis = exam?.result ?: "Sem diagnóstico disponível"

    val borderColor = if (state.isEditable) Color(0xFF4CAF50) else Color.Gray

    // Exame resumo
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Status do Exame:", style = MaterialTheme.typography.labelLarge, color = Color.White)
            Text(status, style = MaterialTheme.typography.bodyLarge, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Diagnóstico:", style = MaterialTheme.typography.labelLarge, color = Color.White)
            Text(diagnosis, style = MaterialTheme.typography.bodyLarge, color = Color.White)
        }
    }

    // Campos de texto
    @Composable
    fun outlinedField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        enabled: Boolean = true,
        trailingIcon: @Composable (() -> Unit)? = null,
        viewModel: PatientDetailViewModel
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, color = Color.White) },
            enabled = enabled,
            readOnly = false, // <- permite edição quando enabled for true
            trailingIcon = trailingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (enabled) Color(0xFF4CAF50) else Color.Gray,
                unfocusedBorderColor = if (enabled) Color(0xFF4CAF50) else Color.Gray,
                disabledTextColor = Color.White,
                disabledLabelColor = Color.White,
                disabledBorderColor = Color.Gray,
                cursorColor = Color(0xFF4CAF50),
                focusedLabelColor = Color(0xFF4CAF50)
            )
        )
    }


    // Nome
    outlinedField("Nome", state.name, { onFieldChange("name", it) },enabled = state.isEditable, viewModel = viewModel)

    // Tipo de Documento
    if (state.isEditable) {
        DropdownField(
            label = "Tipo de Documento",
            options = listOf("CPF", "CNPJ"),
            selected = state.documentType,
            onValueChange = { onFieldChange("documentType", it) },
            enabled = true
        )
    } else {
        outlinedField("Tipo de Documento", state.documentType, {}, false, viewModel = viewModel)
    }

    // Documento
    outlinedField("Documento", state.document, { onFieldChange("document", it) },enabled = state.isEditable, viewModel = viewModel)


        outlinedField("Data de Nascimento", state.birthDate?.let { formatter.format(it) } ?: "", {}, false, viewModel = viewModel)


    // Telefone
    outlinedField("Telefone", formatPhone(state.phone), { onFieldChange("phone", it) },enabled = state.isEditable, viewModel = viewModel)

    // Endereço
    outlinedField("Endereço", state.address, { onFieldChange("address", it) },enabled = state.isEditable, viewModel = viewModel)

    // Gênero
    if (state.isEditable) {
        DropdownField(
            label = "Gênero",
            options = listOf("Masculino", "Feminino", "Outro"),
            selected = state.gender,
            onValueChange = { onFieldChange("gender", it) },
            enabled = true
        )
    } else {
        outlinedField("Gênero", state.gender, {}, false, viewModel = viewModel)
    }

    // Estado Civil
    if (state.isEditable) {
        DropdownField(
            label = "Estado Civil",
            options = listOf("Solteiro", "Casado", "Viúvo", "Separado", "Divorciado"),
            selected = state.maritalStatus,
            onValueChange = { onFieldChange("maritalStatus", it) },
            enabled = true
        )
    } else {
        outlinedField("Estado Civil", state.maritalStatus, {}, false, viewModel = viewModel)
    }

    // Raça
    outlinedField("Raça", state.race, { onFieldChange("race", it) },enabled = state.isEditable, viewModel = viewModel)

    // Escolaridade
    outlinedField("Escolaridade", state.educationLevel, { onFieldChange("educationLevel", it) },enabled = state.isEditable, viewModel = viewModel)

    // Origem
    outlinedField("Origem", state.origin, { onFieldChange("origin", it) },enabled = state.isEditable, viewModel = viewModel)

    // Renda Anual
    outlinedField("Renda Anual", state.annualIncome.toString(), { onFieldChange("annualIncome", it) },enabled = state.isEditable, viewModel = viewModel)

    // Criado em (sempre visualização)
    outlinedField("Criado em", state.createdAt?.let { formatter.format(it) } ?: "", {}, false, viewModel = viewModel)

    // Botões
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (state.isEditable) {
            Button(onClick = { onEditToggle() }, modifier = Modifier.weight(1f)) {
                Text("Cancelar")
            }
            Button(onClick = { onSave() }, modifier = Modifier.weight(1f)) {
                Text("Concluir")
            }
        } else {
            IconButton(
                onClick = { onEditToggle() },
                modifier = Modifier.weight(0.5f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            var showDeleteConfirmation by remember { mutableStateOf(false) }

            IconButton(
                onClick = {
                    showDeleteConfirmation = true
                },
                modifier = Modifier.weight(0.5f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Deletar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Confirmar exclusão") },
                    text = { Text("Tem certeza que deseja excluir permanentemente este paciente? Esta ação não poderá ser desfeita.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteConfirmation = false
                                viewModel.onEvent(PatientDetailIntent.DeletePatient(patientId)) {
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("deletionSuccess", true)
                                    navController.popBackStack("patients", false)
                                }
                            }
                        ) {
                            Text("Excluir", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirmation = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }



            Button(
                onClick = {
                    // diagnosticar
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Diagnosticar")
            }
        }
    }
}



