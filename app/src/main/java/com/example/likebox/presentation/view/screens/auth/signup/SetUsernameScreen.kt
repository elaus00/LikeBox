package com.example.likebox.presentation.view.screens.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun SetUsernameScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var username by remember { mutableStateOf("") }
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()
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
                title = "Set Username",
                subtitle = "Choose your display name"
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthTextField(
                value = username,
                onValueChange = {
                    if (it.length <= 20) username = it
                },
                label = "Username",
                placeholder = "Enter your username"
            )

            Text(
                text = "${username.length}/20",
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            AuthButton(
                text = "Complete",
                onClick = {
                    viewModel.setUsername(username)
                    navController.navigate(Screens.Auth.PlatformSetup.Selection.route)
                },
                enabled = username.isNotEmpty()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}