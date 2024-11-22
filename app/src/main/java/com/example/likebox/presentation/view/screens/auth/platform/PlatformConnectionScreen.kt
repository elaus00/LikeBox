package com.example.likebox.presentation.view.screens.auth.platform

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.likebox.R
import com.example.likebox.presentation.view.navigation.NavigationCommand
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.screens.auth.AuthButton
import com.example.likebox.presentation.view.screens.auth.AuthTitle
import com.example.likebox.presentation.view.screens.auth.CustomSnackbar
import com.example.likebox.presentation.view.screens.auth.LikeBoxTopAppBar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformConnectionScreen(
    platformId: String,
    viewModel: PlatformConnectionViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current as ComponentActivity  // Activity context를 얻습니다
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showPlatformSetupDialog by remember { mutableStateOf(!uiState.isConnected) }

    LaunchedEffect(Unit) {
        viewModel.initializePlatform(platformId)
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short,
                withDismissAction = true,
                actionLabel = "Dismiss"
            )
        }
    }

    // Navigation 처리
    LaunchedEffect(uiState.navigationCommand) {
        uiState.navigationCommand?.let { command ->
            when (command) {
                is NavigationCommand.NavigateToAndClearStack -> {
                    navController.navigate(command.screen.route) {
                        popUpTo(Screens.Auth.OnBoarding.route) { inclusive = true }
                    }
                    viewModel.onNavigationComplete()
                }
                else -> { /* 다른 네비게이션 케이스 처리 */ }
            }
        }
    }

    LaunchedEffect(uiState.isConnected) {
        showPlatformSetupDialog = !uiState.isConnected
    }

    Scaffold(
        topBar = {
            LikeBoxTopAppBar(
                onBackClick = { navController.navigateUp() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                CustomSnackbar(snackbarData = snackbarData)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AuthTitle(
                title = "Connect to Spotify",
                subtitle = "Log in to your Spotify account to sync your music",
                titleFontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Spotify 로고
            Image(
                painter = painterResource(id = R.drawable.spotify_logomark),
                contentDescription = "Spotify Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 연결 버튼
            AuthButton(
                text = if (uiState.isLoading) "Connecting..." else "Connect Spotify",
                onClick = { viewModel.PlatformConnect(context) },
                enabled = !uiState.isLoading,
                isLoading = uiState.isLoading
            )

            // 설명 텍스트
            Text(
                text = "By connecting your account, you agree to share your music data with LikeBox",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }

        // 로딩 오버레이
        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}