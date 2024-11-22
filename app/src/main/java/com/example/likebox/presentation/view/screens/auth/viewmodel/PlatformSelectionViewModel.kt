package com.example.likebox.presentation.view.screens.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformState
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.presentation.view.navigation.NavigationCommand
import com.example.likebox.presentation.view.screens.Screens
import com.example.likebox.presentation.view.screens.auth.state.SyncStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlatformSelectionViewModel @Inject constructor(
    private val platformRepository: PlatformRepository
) : ViewModel() {

    data class UiState(
        val platformStates: Map<MusicPlatform, PlatformState> = emptyMap(),
        val selectedPlatforms: Set<MusicPlatform> = emptySet(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        initializePlatformStates()
        observePlatformStates()
    }

    private fun initializePlatformStates() {
        val initialStates = MusicPlatform.entries.associate { platform ->
            platform to PlatformState.default(platform)
        }
        _uiState.update { it.copy(platformStates = initialStates) }
    }

    private fun observePlatformStates() {
        viewModelScope.launch {
            // 1. Observe connected platforms
            launch {
                platformRepository.getConnectedPlatforms()
                    .onSuccess { platforms ->
                        updateConnectedPlatforms(platforms)
                    }
                    .onFailure { handleError(it) }
            }

            // 2. Observe sync status for each platform
            MusicPlatform.entries.forEach { platform ->
                launch {
                    platformRepository.observeSyncStatus(platform).collect { status ->
                        updatePlatformSyncStatus(platform, status)
                    }
                }
            }
        }
    }

    private fun updatePlatformSyncStatus(platform: MusicPlatform, status: SyncStatus) {
        _uiState.update { state ->
            val currentPlatformState = state.platformStates[platform] ?: return@update state
            val updatedPlatformState = currentPlatformState.copy(
                syncStatus = status,
                lastSyncTime = when (status) {
                    SyncStatus.COMPLETED -> System.currentTimeMillis()
                    SyncStatus.IN_PROGRESS,
                    SyncStatus.ERROR,
                    SyncStatus.IDLE,
                    SyncStatus.NOT_SYNCED -> currentPlatformState.lastSyncTime
                },
                errorMessage = when (status) {
                    SyncStatus.ERROR -> "Failed to sync platform"
                    SyncStatus.IN_PROGRESS,
                    SyncStatus.COMPLETED,
                    SyncStatus.IDLE,
                    SyncStatus.NOT_SYNCED -> null
                }
            )
            state.copy(
                platformStates = state.platformStates + (platform to updatedPlatformState)
            )
        }
    }

    fun togglePlatformSelection(platform: MusicPlatform) {
        val currentState = _uiState.value.platformStates[platform] ?: return

        if (!currentState.isEnabled) {
            setError("This platform will be supported soon!")
            return
        }

        _uiState.update { state ->
            val newSelectedPlatforms = if (platform in state.selectedPlatforms) {
                state.selectedPlatforms - platform
            } else {
                state.selectedPlatforms + platform
            }
            state.copy(selectedPlatforms = newSelectedPlatforms)
        }
    }

    fun connectSelectedPlatforms() {
        val selectedPlatforms = _uiState.value.selectedPlatforms
        if (selectedPlatforms.isEmpty()) {
            setError("Please select at least one platform")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            selectedPlatforms.forEach { platform ->
                platformRepository.updateSyncStatus(platform, SyncStatus.IN_PROGRESS)
                    .onFailure {
                        platformRepository.updateSyncStatus(platform, SyncStatus.ERROR)
                        handleError(it)
                    }

                platformRepository.connectPlatform(platform, "dummy_auth_code")
                    .onSuccess {
                        platformRepository.updateSyncStatus(platform, SyncStatus.COMPLETED)
                    }
                    .onFailure {
                        platformRepository.updateSyncStatus(platform, SyncStatus.ERROR)
                        handleError(it)
                    }
            }

            platformRepository.syncAllPlatforms()
                .onSuccess {
                    selectedPlatforms.forEach { platform ->
                        platformRepository.updateSyncStatus(platform, SyncStatus.COMPLETED)
                    }
                }
                .onFailure { handleError(it) }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun updateConnectedPlatforms(platforms: List<MusicPlatform>) {
        _uiState.update { state ->
            val updatedStates = state.platformStates.mapValues { (platform, currentState) ->
                currentState.copy(
                    isConnected = platform in platforms,
                    syncStatus = if (platform in platforms) SyncStatus.COMPLETED else SyncStatus.NOT_SYNCED
                )
            }
            state.copy(platformStates = updatedStates)
        }
    }

    private fun handleError(throwable: Throwable) {
        setError(throwable.message ?: "Unknown error occurred")
    }

    private fun setError(message: String) {
        _uiState.update { it.copy(error = message, isLoading = false) }
    }

    fun showError(message: String) {
        setError(message)
    }

    fun onPlatformsSelected(platforms: Set<MusicPlatform>) {
        if (platforms.isEmpty()) {
            setError("Please select at least one platform")
            return
        }
        connectSelectedPlatforms()
    }
}
