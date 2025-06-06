package com.android.dc_dtm.features.patient.patientList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.dc_dtm.core.api.RetrofitInstance
import com.android.dc_dtm.core.helper.TokenManager
import com.android.dc_dtm.core.presentation.components.AppScaffoldWithDrawer
import com.android.dc_dtm.features.dashboard.presentation.DashboardViewModel
import com.android.dc_dtm.features.patient.patientList.data.dto.PatientDto
import com.android.dc_dtm.ui.theme.LightGrayTopBar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PatientListScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    

    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    
    
    val viewModel = remember { PatientListViewModel(
        context = context
    ) }

    val state by viewModel.state.collectAsState()
    
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""

    var searchText by remember { mutableStateOf(state.searchQuery) }

    val currentBackStackEntryValue = navController.currentBackStackEntryAsState().value
    val deletionSuccess = currentBackStackEntry
        ?.savedStateHandle
        ?.get<Boolean>("deletionSuccess") == true

    LaunchedEffect(deletionSuccess) {
        if (deletionSuccess) {
            snackbarHostState.showSnackbar("Paciente deletado com sucesso")
            currentBackStackEntryValue?.savedStateHandle?.remove<Boolean>("deletionSuccess")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(PatientListIntent.LoadPatients)
    }

    AppScaffoldWithDrawer(
        navController = navController,
        screenTitle = "Pacientes",
        currentRoute = currentRoute,
        onLogout = {
            tokenManager.clearToken()
            navController.navigate("login") {
                popUpTo("patients") { inclusive = true }
            }
        },
        topBarActions = {
            IconButton(onClick = {
                navController.navigate("createPatient")
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Paciente",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(), // respeita só o top da TopBar
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ) // já cuida do top
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    viewModel.onEvent(PatientListIntent.SearchByName(it))
                },

                label = { Text("Search by name") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.error != null) {
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.patients) { patient ->
                        PatientCard(
                            patient = patient,
                            onViewDetails = {
                                navController.navigate("patientDetails/${patient.id}")
                            },
                            onDoExam = {
                                navController.navigate("doExam/${patient.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PatientCard(
    patient: PatientDto,
    onViewDetails: () -> Unit,
    onDoExam: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val examStatus = patient.exams?.firstOrNull()?.status ?: "No exam"

    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.background(LightGrayTopBar).padding(16.dp)) {
            Text(patient.name, style = MaterialTheme.typography.titleLarge)
            Text("Documento: ${patient.document}")
            Text("Criado em: ${dateFormat.format(patient.createdAt)}")
            Text("Status: $examStatus")

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onViewDetails) {
                    Icon(Icons.Default.Visibility, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Details")
                }

                val examDone = patient.exams?.any { it.status != "pending_validation" } ?: false

                if (!examDone) {
                    Button(onClick = onDoExam) {
                        Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Do Exam")
                    }
                }
            }
        }
    }
}
