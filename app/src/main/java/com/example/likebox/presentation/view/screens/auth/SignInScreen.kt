package com.example.likebox.presentation.view.screens.auth

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.likebox.R
import com.example.likebox.presentation.state.auth.SignInMethod
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.viewmodel.AuthUiEvent
import com.example.likebox.presentation.viewmodel.AuthViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val scope = rememberCoroutineScope()
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is AuthUiEvent.SignInSuccess -> {
                    navController.navigate(Screens.Main.Home.Root.route) {
                        popUpTo(Screens.Auth.SignIn.route) { inclusive = true }
                    }
                }
                is AuthUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true,
                        actionLabel = "Dismiss"
                    )
                }
                else -> {}
            }
        }
    }



    val launcher = rememberLauncherForActivityResult(
        contract = FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.authState
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                CustomSnackbar(snackbarData = snackbarData)
            }
        },
        topBar = {
            TopAppBar(
                title = { },
                modifier = Modifier.padding(start = 12.dp),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
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
                        top = padding.calculateTopPadding().minus(20.dp),
                        bottom = padding.calculateBottomPadding(),
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthTitle(
                title = "Login",
                titleFontWeight = FontWeight(500)
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthToggleButton(
                options = listOf("Email", "Phone Number"),
                selectedOption = if (signInState.signInMethod == SignInMethod.EMAIL) "Email" else "Phone Number",
                onOptionSelected = {
                    viewModel.updateSignInMethod(
                        if (it == "Email") SignInMethod.EMAIL else SignInMethod.PHONE
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (signInState.signInMethod) {
                SignInMethod.EMAIL -> {
                    AuthTextField(
                        value = signInState.email,
                        onValueChange = { viewModel.updateEmail(it) },
                        label = "Email Address",
                        placeholder = "likebox@example.com",
                        error = signInState.emailError
                    )
                }
                SignInMethod.PHONE -> {
                    PhoneNumberTextField(
                        phoneNumber = signInState.phoneNumber,
                        onPhoneNumberChange = { viewModel.updatePhoneNumber(it) },
                        error = signInState.phoneNumberError
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            AuthTextField(
                value = signInState.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = "Password",
                placeholder = "Enter your password",
                isPassword = !signInState.showPassword,
                error = signInState.passwordError,
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (signInState.showPassword) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuthTextButton(
                modifier = Modifier.align(Alignment.End),
                text = "Forgot Password?",
                fontSize = 12.sp,
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "This feature is coming soon",
                            duration = SnackbarDuration.Short,
                            withDismissAction = true,
                            actionLabel = "Dismiss"
                        )
                    }
                },
                textColor = Color.Black.copy(0.8f),
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthButton(
                text = "Login",
                onClick = { viewModel.signIn() },
                enabled = !signInState.isLoading,
                isLoading = signInState.isLoading
            )
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.Black.copy(alpha = 0.2f)
                )
                Text(
                    text = "or continue with",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFF7D7D7D).copy(0.8f),
                    fontSize = 16.sp,
                    fontFamily = PretendardFontFamily
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.Black.copy(alpha = 0.2f)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            SocialLoginButton(
                text = "Continue with Google",
                icon = R.drawable.google_logo,
                onClick = {
                    val providers = arrayListOf(
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )

                    val signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build()

                    launcher.launch(signInIntent)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            AuthTextButton(
                text = "Create an account",
                onClick = { navController.navigate(Screens.Auth.SignUp.Root.route) },
                lineHeight = 40.sp
            )
        }
    }
}