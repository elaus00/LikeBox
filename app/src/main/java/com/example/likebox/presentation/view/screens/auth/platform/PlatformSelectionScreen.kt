package com.example.likebox.presentation.view.screens.auth.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.likebox.presentation.view.screens.auth.AuthButton
import com.example.likebox.presentation.view.screens.auth.AuthTitle
import com.example.likebox.presentation.view.screens.auth.CustomSnackbar
import com.example.likebox.presentation.view.screens.auth.LikeBoxTopAppBar
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformState
import com.example.likebox.presentation.view.screens.auth.state.SyncStatus
import com.example.likebox.presentation.view.screens.auth.viewmodel.PlatformSelectionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformSelectionScreen(
    navController: NavController,
    viewModel: PlatformSelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Error 처리
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                AuthTitle(
                    title = "Select the music platforms you use",
                    subtitle = "You can select multiple platforms",
                    titleFontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(45.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(MusicPlatform.entries.toList()) { platform ->
                        val platformState = uiState.platformStates[platform]
                            ?: PlatformState.default(platform)

                        PlatformItem(
                            platformState = platformState,
                            isSelected = platform in uiState.selectedPlatforms,
                            onClick = { viewModel.togglePlatformSelection(platform) }
                        )
                    }
                }
            }

            AuthButton(
                text = "Next",
                onClick = { viewModel.connectSelectedPlatforms() },
                enabled = uiState.selectedPlatforms.isNotEmpty() && !uiState.isLoading,
                modifier = Modifier.padding(bottom = 30.dp)
            )
        }

        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
private fun PlatformItem(
    platformState: PlatformState,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        !platformState.isEnabled -> Color.Gray.copy(alpha = 0.3f)
        platformState.syncStatus == SyncStatus.IN_PROGRESS -> Color(0xFFE3F2FD)
        isSelected -> Color.White
        else -> Color.White.copy(alpha = 0.75f)
    }

    val borderColor = when {
        !platformState.isEnabled -> Color.Gray.copy(alpha = 0.5f)
        platformState.syncStatus == SyncStatus.IN_PROGRESS -> Color(0xFF2196F3)
        platformState.syncStatus == SyncStatus.COMPLETED -> Color(0xFF4CAF50)
        isSelected -> Color(0xFFF93C58)
        else -> Color(0xA5A2A2A2)
    }

    Surface(
        modifier = Modifier
            .height(68.dp)
            .border(
                width = 0.77.dp,
                color = borderColor,
                shape = RoundedCornerShape(22.95.dp)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(22.95.dp)
            ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = when (platformState.platform) {
                    MusicPlatform.SPOTIFY -> "Spotify"
                    MusicPlatform.APPLE_MUSIC -> "Apple Music"
                    MusicPlatform.YOUTUBE_MUSIC -> "YouTube Music"
                    MusicPlatform.MELON -> "Melon"
                    MusicPlatform.GENIE -> "Genie"
                    MusicPlatform.FLOO -> "FLO"
                    MusicPlatform.TIDAL -> "Tidal"
                    MusicPlatform.AMAZON_MUSIC -> "Amazon Music"
                },
                fontFamily = PretendardFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = when {
                    !platformState.isEnabled -> Color.Gray
                    platformState.syncStatus == SyncStatus.ERROR -> Color.Red
                    platformState.syncStatus == SyncStatus.IN_PROGRESS -> Color(0xFF2196F3)
                    platformState.syncStatus == SyncStatus.COMPLETED -> Color(0xFF4CAF50)
                    isSelected -> Color(0xFFF93C58)
                    else -> Color.Black
                }
            )

            if (platformState.syncStatus == SyncStatus.IN_PROGRESS) {
                Spacer(modifier = Modifier.height(4.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = Color(0xFF2196F3)
                )
            }

            platformState.lastSyncTime?.let { timestamp ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Last sync: ${formatTimestamp(timestamp)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (platformState.syncStatus == SyncStatus.ERROR) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = platformState.errorMessage ?: "Sync failed",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
private fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.White
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    return SimpleDateFormat("MM.dd HH:mm", Locale.getDefault())
        .format(Date(timestamp))
}