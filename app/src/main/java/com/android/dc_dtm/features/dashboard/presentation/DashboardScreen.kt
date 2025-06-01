package com.android.dc_dtm.features.dashboard.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.dc_dtm.core.helper.TokenManager
import com.android.dc_dtm.core.presentation.components.AppScaffoldWithDrawer
import com.android.dc_dtm.ui.theme.LightGrayTopBar

@Composable
fun DashboardScreen(navController: NavController) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val token = tokenManager.getToken()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""

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

        // Cor do top bar
        val boxColor = LightGrayTopBar

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Linha 1
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoCard(title = "Total de pacientes:", number = 120, color = boxColor, modifier = Modifier.weight(1f))
                InfoCard(title = "Total de diagnósticos:", number = 75, color = boxColor, modifier = Modifier.weight(1f))
            }

            // Linha 2
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoCard(title = "Diagnósticos pendentes:", number = 10, color = boxColor, modifier = Modifier.weight(1f))
                InfoCard(title = "Diagnósticos validados:", number = 65, color = boxColor, modifier = Modifier.weight(1f))
            }

            // Linha 3 só 1 quadrado, para o último
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoCard(title = "Total de colaboradores:", number = 25, color = boxColor, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(1f)) // Preenche o espaço vazio para alinhar os quadrados da linha acima
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

