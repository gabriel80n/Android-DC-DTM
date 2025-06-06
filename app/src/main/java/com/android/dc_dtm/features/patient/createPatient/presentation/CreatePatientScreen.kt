package com.android.dc_dtm.features.patient.createPatient.presentation

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.dc_dtm.core.presentation.components.SimpleScaffold
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CreatePatientScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { CreatePatientViewModel(context) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.popBackStack() // volta para a tela anterior (PatientList)
        }
    }

    SimpleScaffold(
        title = "Criar Paciente",
        onBackClick = onBackClick
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                item {
                    // Nome
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "name",
                                    it
                                )
                            )
                        },
                        label = { Text("Nome") },
                        isError = state.fieldErrors["name"] != null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                    state.fieldErrors["name"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Tipo de Documento
                    DropdownField(
                        label = "Tipo de Documento",
                        options = listOf("CPF", "CNPJ"),
                        selected = state.documentType,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "documentType",
                                    it
                                )
                            )
                        },
                        enabled = true,
                        isError = state.fieldErrors["documentType"] != null
                    )
                    state.fieldErrors["documentType"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Documento
                    OutlinedTextField(
                        value = state.document,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "document",
                                    it
                                )
                            )
                        },
                        label = { Text("Documento") },
                        isError = state.fieldErrors["document"] != null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                    state.fieldErrors["document"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Data de nascimento
                    Column {
                        DatePickerField(
                            label = "Data de Nascimento",
                            selectedDate = state.birthDate,
                            onDateSelected = {
                                viewModel.onEvent(
                                    CreatePatientIntent.UpdateField(
                                        "birthDate",
                                        it
                                    )
                                )
                            },
                            isError = state.fieldErrors["birthDate"] != null
                        )
                        state.fieldErrors["birthDate"]?.let { errorMsg ->
                            Text(
                                text = errorMsg,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                            )
                        }
                    }

                    // Telefone
                    OutlinedTextField(
                        value = formatPhone(state.phone),
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "phone",
                                    it
                                )
                            )
                        },
                        label = { Text("Telefone") },
                        isError = state.fieldErrors["phone"] != null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                    state.fieldErrors["phone"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Endereço
                    OutlinedTextField(
                        value = state.address,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "address",
                                    it
                                )
                            )
                        },
                        label = { Text("Endereço") },
                        isError = state.fieldErrors["address"] != null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                    state.fieldErrors["address"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Gênero
                    DropdownField(
                        label = "Gênero",
                        options = listOf("Masculino", "Feminino", "Outro"),
                        selected = state.gender,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "gender",
                                    it
                                )
                            )
                        },
                        enabled = true,
                        isError = state.fieldErrors["gender"] != null
                    )
                    state.fieldErrors["gender"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Estado Civil
                    DropdownField(
                        label = "Estado Civil",
                        options = listOf("Solteiro", "Casado", "Viúvo", "Separado", "Divorciado"),
                        selected = state.maritalStatus,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "maritalStatus",
                                    it
                                )
                            )
                        },
                        enabled = true,
                        isError = state.fieldErrors["maritalStatus"] != null
                    )
                    state.fieldErrors["maritalStatus"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Raça
                    OutlinedTextField(
                        value = state.race,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "race",
                                    it
                                )
                            )
                        },
                        label = { Text("Raça") },
                        isError = state.fieldErrors["race"] != null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                    state.fieldErrors["race"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Escolaridade
                    OutlinedTextField(
                        value = state.educationLevel,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "educationLevel",
                                    it
                                )
                            )
                        },
                        label = { Text("Escolaridade") },
                        isError = state.fieldErrors["educationLevel"] != null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                    state.fieldErrors["educationLevel"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Origem
                    OutlinedTextField(
                        value = state.origin,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "origin",
                                    it
                                )
                            )
                        },
                        label = { Text("Origem") },
                        isError = state.fieldErrors["origin"] != null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                    state.fieldErrors["origin"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    // Renda Anual
                    OutlinedTextField(
                        value = state.annualIncome,
                        onValueChange = {
                            viewModel.onEvent(
                                CreatePatientIntent.UpdateField(
                                    "annualIncome",
                                    it
                                )
                            )
                        },
                        label = { Text("Renda Anual") },
                        isError = state.fieldErrors["annualIncome"] != null,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                    state.fieldErrors["annualIncome"]?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botões
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Cancelar")
                        }
                        Button(onClick = { viewModel.onEvent(CreatePatientIntent.Submit) }) {
                            Text("Criar")
                        }
                    }

                    // Loading e erros
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    state.error?.let {
                        Text("Erro: $it", color = MaterialTheme.colorScheme.error)
                    }
                }

            }
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

    }
}



@Composable
fun DatePickerField(
    label: String,
    selectedDate: Date?,
    onDateSelected: (String) -> Unit,
    isError: Boolean = false
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val formattedDate = selectedDate?.let { formatter.format(it) } ?: ""

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                val selected = calendar.time
                onDateSelected(formatter.format(selected))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() }
    ) {
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Selecionar data",
                    tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )

            },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            colors = if (isError) {
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.error,
                    unfocusedBorderColor = MaterialTheme.colorScheme.error,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.error,
                    unfocusedLabelColor = MaterialTheme.colorScheme.error,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.error,
                    disabledLabelColor = MaterialTheme.colorScheme.error,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            } else {
                OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        )
    }
}



@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selected: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text(label, color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled) { expanded = true },
            enabled = enabled,
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                cursorColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
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




fun formatPhone(input: String): String {
    val digits = input.filter { it.isDigit() }
    return if (digits.length >= 11) {
        "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7, 11)}"
    } else {
        input
    }
}

