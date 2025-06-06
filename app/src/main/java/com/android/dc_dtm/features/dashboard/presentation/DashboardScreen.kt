package com.android.dc_dtm.features.dashboard.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.android.dc_dtm.ui.theme.LightGrayTopBar

@Composable
fun DashboardScreen(
    navController: NavController,// Recebe o ViewModel já criado (injetado ou passado)
) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)

    val dashboardApi = RetrofitInstance.getDashboardApi(context)
    val viewModel = remember { DashboardViewModel(dashboardApi) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""

    // Observe o estado atual do ViewModel
    val state by viewModel.state.collectAsState()

    // Chama a intent para buscar os dados apenas uma vez na composição inicial
    LaunchedEffect(Unit) {
        viewModel.onEvent(DashboardIntent.GetStats)
    }

    AppScaffoldWithDrawer(
        navController = navController,
        screenTitle = "Dashboard",
        currentRoute = currentRoute,
        onLogout = {
            tokenManager.clearToken()
            navController.navigate("login") {
                popUpTo("dashboard") { inclusive = true }
            }
        }
    ) { innerPadding ->

        val boxColor = LightGrayTopBar

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mostrar carregando, erro ou os dados
            when {
                state.isLoading -> {
                    // Exemplo simples de indicador de loading
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    // Mostra a mensagem de erro
                    Text(
                        text = state.error ?: "Erro desconhecido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    // Linha 1
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InfoCard(
                            title = "Total de pacientes:",
                            number = state.totalPacients,
                            color = boxColor,
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            title = "Total de diagnósticos:",
                            number = state.totalDiagnostics,
                            color = boxColor,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Linha 2
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InfoCard(
                            title = "Diagnósticos pendentes:",
                            number = state.pendingDiagnostics,
                            color = boxColor,
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            title = "Diagnósticos validados:",
                            number = state.validatedDiagnostics,
                            color = boxColor,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Linha 3
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InfoCard(
                            title = "Total de colaboradores:",
                            number = state.totalUsers,
                            color = boxColor,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}



@Composable
fun InfoCard(
    title: String,
    number: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(120.dp),
        color = color,
        shape = MaterialTheme.shapes.medium.copy(all = androidx.compose.foundation.shape.CornerSize(12.dp)),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

