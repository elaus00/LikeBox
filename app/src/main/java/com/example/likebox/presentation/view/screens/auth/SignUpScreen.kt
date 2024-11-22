package com.example.likebox.presentation.view.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.likebox.presentation.view.screens.auth.state.SignUpMethod
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.theme.mainColor
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent
    var verificationCode by rememberSaveable { mutableStateOf("") }
    var remainingTime by rememberSaveable { mutableIntStateOf(60) }

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is AuthUiEvent.NavigateToPlatformSetup -> {
                    navController.navigate(Screens.Auth.PlatformSetup.Selection.route) {
                        popUpTo(Screens.Auth.SignUp.Root.route) { inclusive = true }
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

    LaunchedEffect(remainingTime) {
        if (remainingTime > 0) {
            delay(1000)
            remainingTime--
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                    CustomSnackbar(snackbarData = snackbarData)
                }
            },
            topBar = {
                LikeBoxTopAppBar(
                    onBackClick = { navController.navigateUp() }
                )
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
                    subtitle = null
                )

                Spacer(modifier = Modifier.height(32.dp))

                when (signUpState.signUpMethod) {
                    SignUpMethod.EMAIL -> {
                        AuthTextField(
                            value = signUpState.emailOrPhone,
                            onValueChange = { viewModel.updateEmailOrPhone(it) },
                            label = "Email Address",
                            placeholder = "likebox@example.com",
                            trailingContent = {
                                AuthTextButton(
                                    text = "Continue with Phone Number",
                                    onClick = {
                                        viewModel.updateSignUpMethod(SignUpMethod.PHONE)
                                    },
                                    fontWeight = FontWeight.Bold,
                                    textColor = mainColor.copy(0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 40.sp
                                )
                            }
                        )
                    }
                    SignUpMethod.PHONE -> {
                        AuthTextField(
                            value = signUpState.emailOrPhone,
                            onValueChange = { viewModel.updateEmailOrPhone(it) },
                            label = "Phone Number",
                            placeholder = "010-1234-5678",
                            trailingContent = {
                                AuthTextButton(
                                    text = "Continue with Email Address",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    onClick = {
                                        viewModel.updateSignUpMethod(SignUpMethod.EMAIL)
                                    },
                                    textColor = mainColor.copy(0.8f),
                                    lineHeight = 40.sp
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        if (signUpState.isVerificationSent) {
                            Spacer(modifier = Modifier.height(8.dp))

                            VerificationTextField(
                                value = verificationCode,
                                onValueChange = { verificationCode = it }
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            VerificationResendButton(
                                onClick = {
                                    remainingTime = 60
                                },
                                remainingTime = remainingTime
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = signUpState.password,
                    onValueChange = { viewModel.setPassword(it) },
                    label = "Password",
                    placeholder = "Enter your password",
                    isPassword = !signUpState.showPassword,
                    error = signUpState.passwordError,
                    trailingIcon = {
                        IconButton(onClick = { viewModel.toggleSignUpPasswordVisibility() }) {
                            Icon(
                                imageVector = if (signUpState.showPassword) {
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

                AuthTextField(
                    value = signUpState.passwordConfirmation,
                    onValueChange = { viewModel.setPasswordConfirmation(it) },
                    label = "Confirm Password",
                    placeholder = "Confirm your password",
                    isPassword = !signUpState.showPasswordConfirmation,
                    error = signUpState.passwordConfirmationError,
                    trailingIcon = {
                        IconButton(onClick = { viewModel.toggleSignUpPasswordConfirmationVisibility() }) {
                            Icon(
                                imageVector = if (signUpState.showPasswordConfirmation) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = "Toggle password confirmation visibility"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = signUpState.username,
                    onValueChange = { viewModel.setUsername(it) },
                    label = "Username",
                    placeholder = "Enter your username"
                )

                Spacer(modifier = Modifier.height(32.dp))

                AuthButton(
                    text = "Sign Up",
                    onClick = {
                        when (signUpState.signUpMethod) {
                            SignUpMethod.EMAIL -> {
                                viewModel.signUpWithEmail(
                                    signUpState.emailOrPhone,
                                    signUpState.password,
                                    signUpState.username
                                )
                            }
                            SignUpMethod.PHONE -> {
                                if (signUpState.isVerificationSent) {
                                    viewModel.verifyPhoneCode(verificationCode)
                                } else {
                                    viewModel.signUpWithPhoneNumber(
                                        signUpState.emailOrPhone,
                                        context as android.app.Activity,
                                        signUpState.password
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}