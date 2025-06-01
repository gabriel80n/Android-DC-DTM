package com.android.dc_dtm

import android.annotation.SuppressLint
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.dc_dtm.core.api.RetrofitInstance

import com.android.dc_dtm.core.presentation.SharedAuthViewModel
import com.android.dc_dtm.features.dashboard.presentation.DashboardScreen
import com.android.dc_dtm.features.login.confirm_email.presentation.ConfirmEmailScreen
import com.android.dc_dtm.features.login.forgot_password.presentation.ForgotPasswordScreen
import com.android.dc_dtm.ui.theme.DCDTMTheme
import features.login.domain.usecase.LoginUseCase
import features.login.presentation.LoginViewModel
import com.android.dc_dtm.features.login.login.presentation.LoginScreen

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginApi = RetrofitInstance.getLoginApi(this)
        enableEdgeToEdge() // para layout imersivo

        setContent {
            DCDTMTheme {
                val navController = rememberNavController()
                val sharedAuthViewModel = remember { SharedAuthViewModel() }
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            // Instancia dependências (ideal usar DI, mas assim já funciona)
                            val context = LocalContext.current
                            val repository =
                                com.android.dc_dtm.features.login.login.data.repository.AuthRepositoryImpl(
                                    loginApi,
                                    context
                                )
                            val useCase = LoginUseCase(repository)
                            val viewModel = LoginViewModel(useCase)
                            LoginScreen(
                                viewModel,
                                navController,
                                snackbarHostState = snackbarHostState
                            ) {
                                // Navega para dashboard e limpa backstack do login
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                        composable("dashboard") {
                            DashboardScreen(navController)
                        }
                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                navController = navController,
                                sharedAuthViewModel = sharedAuthViewModel,
                                snackbarHostState = snackbarHostState
                            )
                        }
                        composable("confirm_email") {
                            ConfirmEmailScreen(
                                navController = navController,
                                sharedAuthViewModel = sharedAuthViewModel,
                                snackbarHostState = snackbarHostState
                            )
                        }

                    }
                }
            }
        }
    }
}
