package com.android.dc_dtm.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ExitToApp


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.dc_dtm.ui.theme.LightGrayTopBar
import kotlinx.coroutines.launch

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffoldWithDrawer(
    navController: NavController,
    screenTitle: String,
    currentRoute: String,
    onLogout: () -> Unit,
    topBarActions: @Composable (RowScope.() -> Unit)? = null, // <- Novo parâmetro
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val menuItems = listOf(
        MenuItem("Dashboard", "dashboard", Icons.Filled.Dashboard),
        MenuItem("Pacientes", "patients", Icons.Filled.Person),
        MenuItem("Diagnósticos", "diagnostics", Icons.Filled.HealthAndSafety),
        MenuItem("Colaboradores", "employees", Icons.Filled.Group),
        MenuItem("Perfil", "profile", Icons.Filled.AccountCircle),
        MenuItem("Termos e Condições", "terms", Icons.Filled.Description)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxWidth(0.85f),

            ) {
                Spacer(Modifier.height(16.dp))
                menuItems.forEach { (label, route, icon) ->
                    val isSelected = currentRoute == route
                    NavigationDrawerItem(
                        label = { Text(label) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo("dashboard") { inclusive = false }
                                launchSingleTop = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) Color.Green else MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                            .background(
                                color = Color.Transparent,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable {
                                navController.navigate(route) {
                                    popUpTo("dashboard") { inclusive = false }
                                    launchSingleTop = true
                                }
                                scope.launch { drawerState.close() }
                            }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Sair") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .background(
                            color = Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable {
                            scope.launch { drawerState.close() }
                            onLogout()
                        }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(bottom = 8.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Text(
                                text = screenTitle,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        topBarActions?.invoke(this) // <- Aqui insere ações customizadas, se houver
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = LightGrayTopBar
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                content(innerPadding)
            }
        }
    }
}
