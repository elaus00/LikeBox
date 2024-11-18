package com.example.likebox.presentation.view.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.likebox.R
import com.example.likebox.presentation.state.AuthState
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.theme.LikeBoxTheme
import com.example.likebox.presentation.view.theme.RegisterButton
import com.example.likebox.presentation.viewmodel.AuthViewModel

@Composable
fun OnBoardingScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var showSplash by remember { mutableStateOf(true) }
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(Screens.Main.Home.Root.route) {
                    popUpTo(Screens.Auth.OnBoarding.route) { inclusive = true }
                }
            }
            is AuthState.NeedsPlatformSetup -> {
                navController.navigate(Screens.Auth.PlatformSetup.Selection.route)
            }
            else -> { /* 현재 화면 유지 */ }
        }
    }

    if (showSplash) {
        SplashScreen(onSplashComplete = { showSplash = false })
    } else {
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
                subtitle = "All your favorite songs and playlists in one place",
                modifier = Modifier.padding(top = 115.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                AuthButton(
                    text = "Login",
                    onClick = { navController.navigate(Screens.Auth.SignIn.route) }
                )

                AuthTextButton(
                    text = "Create an account",
                    onClick = { navController.navigate(Screens.Auth.SignUp.route) }
                )
            }
        }
    }
}