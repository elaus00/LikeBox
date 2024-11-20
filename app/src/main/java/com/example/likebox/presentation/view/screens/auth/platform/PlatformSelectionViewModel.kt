package com.example.likebox.presentation.view.screens.auth.platform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.presentation.view.navigation.NavigationCommand
import com.example.likebox.presentation.view.screens.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlatformSelectionViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<PlatformSelectionUiState>(PlatformSelectionUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _navigationCommand = MutableSharedFlow<NavigationCommand>()
    val navigationCommand = _navigationCommand.asSharedFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun onPlatformsSelected(platforms: Set<MusicPlatform>) {
        viewModelScope.launch {
            if (platforms.isEmpty()) {
                _errorMessage.value = "Please select at least one platform"
                return@launch
            }

            // Save selected platforms and navigate to next screen
            // Navigation will be handled by the composable
            _navigationCommand.emit(
                NavigationCommand.NavigateTo(
                    screen = Screens.Auth.PlatformSetup.Connection
                )
            )
        }
    }

    fun showError(message: String) {
        _errorMessage.value = message
    }
}

sealed class PlatformSelectionUiState {
    object Initial : PlatformSelectionUiState()
    object Loading : PlatformSelectionUiState()
    data class Error(val message: String) : PlatformSelectionUiState()
}