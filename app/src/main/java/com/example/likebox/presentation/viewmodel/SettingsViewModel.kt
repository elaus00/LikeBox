package com.example.likebox.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.model.Settings
import com.example.likebox.domain.repository.SettingsRepository
import com.example.likebox.presentation.state.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        loadSettings()
    }

    fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings()
                .catch { error ->
                    _uiState.value = SettingsUiState.Error(error.message ?: "Unknown error")
                }
                .collect { settings ->
                    _uiState.value = SettingsUiState.Success(settings)
                }
        }
    }

    fun updateSettings(settings: Settings) {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            settingsRepository.updateSettings(settings)
                .onSuccess { loadSettings() }
                .onFailure { error ->
                    _uiState.value = SettingsUiState.Error(error.message ?: "Failed to update settings")
                }
        }
    }

    fun exportData() {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            settingsRepository.exportUserData()
                .onSuccess { jsonData ->
                    _uiState.value = SettingsUiState.ExportSuccess(jsonData)
                }
                .onFailure { error ->
                    _uiState.value = SettingsUiState.Error(error.message ?: "Export failed")
                }
        }
    }

    fun importData(jsonData: String) {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            settingsRepository.importUserData(jsonData)
                .onSuccess { loadSettings() }
                .onFailure { error ->
                    _uiState.value = SettingsUiState.Error(error.message ?: "Import failed")
                }
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            settingsRepository.resetSettings()
                .onSuccess { loadSettings() }
                .onFailure { error ->
                    _uiState.value = SettingsUiState.Error(error.message ?: "Reset failed")
                }
        }
    }
}