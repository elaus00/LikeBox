package com.example.likebox.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.likebox.presentation.view.navigation.NavigationHost
import com.example.likebox.presentation.view.theme.LikeBoxTheme
import com.example.likebox.presentation.viewmodel.NavigationViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge 디스플레이 설정
        enableEdgeToEdge()

        setContent {
            LikeBoxTheme {
                // 시스템 바 설정
                val systemUiController = rememberSystemUiController()
                val darkTheme = isSystemInDarkTheme()

                DisposableEffect(systemUiController, darkTheme) {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !darkTheme
                    )
                    onDispose {}
                }

                // Surface를 Scaffold로 변경하여 시스템 바와의 통합을 개선
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { paddingValues ->
                    val navController = rememberNavController()
                    val viewModel: NavigationViewModel = hiltViewModel()
                    val isLoading by viewModel.isLoading.collectAsState()

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)  // 시스템 바에 의한 padding 적용
                    ) {
                        if (isLoading) {
                            LoadingScreen()
                        } else {
                            NavigationHost(
                                navController = navController,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}