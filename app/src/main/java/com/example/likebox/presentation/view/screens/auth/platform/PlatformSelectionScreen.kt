package com.example.likebox.presentation.view.screens.auth.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.example.likebox.presentation.view.navigation.LikeboxNavigationBar
import com.example.likebox.presentation.view.screens.auth.AuthButton
import com.example.likebox.presentation.view.screens.auth.AuthTitle
import com.example.likebox.presentation.view.screens.auth.CustomSnackbar
import com.example.likebox.presentation.view.screens.auth.LikeBoxTopAppBar
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.domain.model.library.MusicPlatform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformSelectionScreen(
    navController: NavController,
    viewModel: PlatformSelectionViewModel = hiltViewModel()
) {
    val selectedPlatforms = remember { mutableStateOf(setOf<MusicPlatform>()) }
    val snackbarHostState = remember { SnackbarHostState() }

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
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(45.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(MusicPlatform.entries.size) { index ->
                        val platform = MusicPlatform.entries[index]
                        PlatformSelectionItem(
                            platform = platform,
                            isSelected = platform in selectedPlatforms.value,
                            onSelect = {
                                selectedPlatforms.value = if (platform in selectedPlatforms.value) {
                                    selectedPlatforms.value - platform
                                } else {
                                    selectedPlatforms.value + platform
                                }
                            }
                        )
                    }
                }
            }

            AuthButton(
                text = "Next",
                onClick = {
                    if (selectedPlatforms.value.isEmpty()) {
                        // Show error message using SnackBar
                        viewModel.showError("Please select at least one platform")
                    } else {
                        viewModel.onPlatformsSelected(selectedPlatforms.value)
                    }
                },
                modifier = Modifier.padding(bottom = 30.dp)
            )
        }
    }
}

@Composable
private fun PlatformSelectionItem(
    platform: MusicPlatform,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(68.dp)
            .border(
                width = 0.77.dp,
                color = if (isSelected) {
                    Color(0xFFF93C58)
                } else {
                    Color(0xA5A2A2A2)
                },
                shape = RoundedCornerShape(22.95.dp)
            )
            .background(
                color = if (isSelected) {
                    Color.White
                } else {
                    Color.White.copy(alpha = 0.75f)
                },
                shape = RoundedCornerShape(22.95.dp)
            ),
        shadowElevation = if (isSelected) 4.dp else 0.dp,
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.13.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (platform) {
                    MusicPlatform.SPOTIFY -> "Spotify"
                    MusicPlatform.APPLE_MUSIC -> "Apple Music"
                },
                fontFamily = PretendardFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color(0xFFF93C58) else Color.Black
            )
        }
    }
}