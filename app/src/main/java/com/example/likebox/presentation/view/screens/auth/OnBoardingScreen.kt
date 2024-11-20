package com.example.likebox.presentation.view.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.likebox.R
import com.example.likebox.presentation.view.screens.auth.state.AuthState
import com.example.likebox.presentation.view.screens.Screens

@Composable
fun OnBoardingScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    var showPlatformSetupDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(Screens.Main.Home.Root.route) {
                    popUpTo(Screens.Auth.OnBoarding.route) { inclusive = true }
                }
            }
            is AuthState.NeedsPlatformSetup -> {
                showPlatformSetupDialog = true
            }
            else -> { /* 현재 화면 유지 */ }
        }
    }

    PlatformSetupDialog(
        showDialog = showPlatformSetupDialog,
        onDismiss = {
            showPlatformSetupDialog = false
            navController.navigate(Screens.Main.Home.Root.route) {
                popUpTo(Screens.Auth.OnBoarding.route) { inclusive = true }
            }
        },
        onNavigateToSetup = {
            showPlatformSetupDialog = false
            navController.navigate(Screens.Auth.PlatformSetup.Selection.route)
        },
        onSkipSetup = {
            showPlatformSetupDialog = false
            navController.navigate(Screens.Main.Home.Root.route) {
                popUpTo(Screens.Auth.OnBoarding.route) { inclusive = true }
            }
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(horizontal = 28.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .padding(top = 160.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.gallery),
                contentDescription = null,
                modifier = Modifier.requiredWidth(575.dp)
            )
        }

        AuthTitle(
            title = "Welcome to LikeBox",
            titleFontWeight = FontWeight.SemiBold,
            subtitle = "All your favorite music contents in one place",
            modifier = Modifier.padding(top = 115.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(vertical = 36.dp)
        ) {
            AuthButton(
                text = "Login",
                onClick = { navController.navigate(Screens.Auth.SignIn.route) }
            )

            AuthTextButton(
                text = "Create an account",
                onClick = { navController.navigate(Screens.Auth.SignUp.Root.route) },
                lineHeight = 40.sp
            )
        }
    }
}