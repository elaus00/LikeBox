package com.example.likebox.presentation.view.screens.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.likebox.presentation.state.auth.SignUpMethod
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.screens.auth.AuthButton
import com.example.likebox.presentation.view.screens.auth.AuthTitle
import com.example.likebox.presentation.view.screens.auth.LikeBoxTopAppBar
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.viewmodel.AuthUiEvent
import com.example.likebox.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var verificationCode by remember { mutableStateOf("") }
    var remainingTime by remember { mutableStateOf(60) }
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime--
        }
    }

    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is AuthUiEvent.NavigateToPlatformSetup -> {
                navController.navigate(Screens.Auth.SignUp.SetPassword.route) {
                    popUpTo(Screens.Auth.SignUp.Verification.route) { inclusive = true }
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
                title = "Verification",
                subtitle = when (signUpState.signUpMethod) {
                    SignUpMethod.EMAIL -> "Code was sent to your email"
                    SignUpMethod.PHONE -> "Code was sent to your phone number"
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            VerificationTextField(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${signUpState.emailOrPhone}로 전송된 인증번호를 입력해주세요.",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = PretendardFontFamily,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            VerificationResendButton(
                onClick = {
                    remainingTime = 60
                    // TODO: Resend verification code
                },
                remainingTime = remainingTime,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.weight(1f))

            AuthButton(
                text = "Next",
                onClick = {
                    // TODO: Verify code and navigate to platform setup screenviewModel.
                    viewModel.verifyPhoneCode(verificationCode)
                },
                enabled = verificationCode.length == 6
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}