package com.example.likebox.presentation.view.screens.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.screens.auth.AuthButton
import com.example.likebox.presentation.view.screens.auth.AuthTextField
import com.example.likebox.presentation.view.screens.auth.AuthTitle
import com.example.likebox.presentation.view.screens.auth.LikeBoxTopAppBar
import com.example.likebox.presentation.view.screens.auth.PasswordField
import com.example.likebox.presentation.viewmodel.AuthUiEvent
import com.example.likebox.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var signUpMethod by remember { mutableStateOf("Email") }
    var phoneNumber by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is AuthUiEvent.NavigateToPlatformSetup -> {
                navController.navigate(Screens.Auth.PlatformSetup.Selection.route) {
                    popUpTo(Screens.Auth.SignUp.Root.route) { inclusive = true }
                }
            }
            is AuthUiEvent.ShowError -> {
                snackbarHostState.showSnackbar(
                    message = (uiEvent as AuthUiEvent.ShowError).message
                )
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LikeBoxTopAppBar(onBackClick = { navController.navigateUp() })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthTitle(
                title = "Create Account",
                subtitle = "Sign up with your email"
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                placeholder = "likebox@example.com"
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordField(
                password = password,
                onPasswordChange = { password = it },
                showPassword = showPassword,
                onTogglePassword = { showPassword = !showPassword },
                label = "Password"
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordField(
                password = confirmPassword,
                onPasswordChange = { confirmPassword = it },
                showPassword = showConfirmPassword,
                onTogglePassword = { showConfirmPassword = !showConfirmPassword },
                label = "Confirm Password"
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuthTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                placeholder = "Enter your username"
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthButton(
                text = "Sign Up",
                onClick = {
                    if (password == confirmPassword) {
                        viewModel.signUpWithEmail(email, password, username)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Passwords do not match")
                        }
                    }
                }
            )
        }
    }
}