package com.example.likebox.presentation.view.screens.settings

import com.example.likebox.domain.model.Settings

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: Settings) : SettingsUiState
    data class Error(val message: String) : SettingsUiState
    data class ExportSuccess(val jsonData: String) : SettingsUiState
}