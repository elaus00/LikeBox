package com.example.likebox.presentation.view.screens.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.likebox.R
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.screens.auth.AuthButton
import com.example.likebox.presentation.view.screens.auth.AuthTextField
import com.example.likebox.presentation.view.screens.auth.AuthTitle
import com.example.likebox.presentation.view.screens.auth.LikeBoxTopAppBar
import com.example.likebox.presentation.viewmodel.AuthUiEvent
import com.example.likebox.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()

    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is AuthUiEvent.NavigateToPlatformSetup -> {
                navController.navigate(Screens.Auth.SignUp.SetUsername.route) {
                    popUpTo(Screens.Auth.SignUp.SetPassword.route) { inclusive = true }
                }
            }
            is AuthUiEvent.ShowError -> {
                snackbarHostState.showSnackbar(
                    message = (uiEvent as AuthUiEvent.ShowError).message,
                    duration = SnackbarDuration.Short
                )
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LikeBoxTopAppBar(
                onBackClick = { navController.navigateUp() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = 30.dp,
                        end = 30.dp,
                        top = padding.calculateTopPadding().minus(16.dp),
                        bottom = padding.calculateBottomPadding(),
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthTitle(
                title = "Set Password",
                subtitle = "Create a secure password"
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Enter your password",
                isPassword = !showPassword,
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            AuthTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                placeholder = "Re-enter your password",
                isPassword = !showConfirmPassword,
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )

            if (password != confirmPassword && confirmPassword.isNotEmpty()) {
                Text(
                    text = "Passwords do not match",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AuthButton(
                text = "Next",
                onClick = {
                    viewModel.setPassword(password)
                    navController.navigate(Screens.Auth.SignUp.SetUsername.route)
                },
                enabled = password.isNotEmpty() && password == confirmPassword
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}