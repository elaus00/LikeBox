package com.example.likebox.presentation.view.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.likebox.domain.model.*
import com.example.likebox.presentation.state.SettingsUiState
import com.example.likebox.presentation.view.navigation.LikeboxNavigationBar
import com.example.likebox.presentation.view.theme.PretendardFontFamily
import com.example.likebox.presentation.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    var showImportDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { SettingsTopBar() },
        bottomBar = { LikeboxNavigationBar(navController) },
        containerColor = Color.White
    ) { padding ->
        when (val state = uiState) {
            is SettingsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SettingsUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        ThemeSection(
                            themeSettings = state.settings.theme,
                            onThemeSettingsChanged = { newThemeSettings ->
                                viewModel.updateSettings(state.settings.copy(theme = newThemeSettings))
                            }
                        )
                    }

                    item {
                        PlatformSection(
                            syncSettings = state.settings.sync,
                            onSyncSettingsChanged = { newSyncSettings ->
                                viewModel.updateSettings(state.settings.copy(sync = newSyncSettings))
                            }
                        )
                    }

                    item {
                        SyncSection(
                            syncSettings = state.settings.sync,
                            onSyncSettingsChanged = { newSyncSettings ->
                                viewModel.updateSettings(state.settings.copy(sync = newSyncSettings))
                            }
                        )
                    }

                    item {
                        DataManagementSection(
                            onExportClick = { showExportDialog = true },
                            onImportClick = { showImportDialog = true },
                            onResetClick = { showResetDialog = true }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }

                // Dialogs
                if (showImportDialog) {
                    ImportDialog(
                        onDismiss = { showImportDialog = false },
                        onImport = { jsonData ->
                            scope.launch {
                                viewModel.importData(jsonData)
                                showImportDialog = false
                            }
                        }
                    )
                }

                if (showExportDialog) {
                    ExportDialog(
                        onDismiss = { showExportDialog = false },
                        onExport = {
                            scope.launch {
                                viewModel.exportData()
                                showExportDialog = false
                            }
                        }
                    )
                }

                if (showResetDialog) {
                    ResetSettingsDialog(
                        onConfirm = {
                            scope.launch {
                                viewModel.resetSettings()
                                showResetDialog = false
                            }
                        },
                        onDismiss = { showResetDialog = false }
                    )
                }
            }
            is SettingsUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    onRetry = { viewModel.loadSettings() }
                )
            }
            is SettingsUiState.ExportSuccess -> {
                // Handle export success (e.g., share the JSON data)
                LaunchedEffect(state.jsonData) {
                    // Implementation for sharing or saving the exported data
                }
            }
        }
    }
}

// TODO : 프로필 설정 기능 들어가야 함.