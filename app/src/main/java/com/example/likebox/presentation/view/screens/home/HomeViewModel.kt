package com.example.likebox.presentation.view.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.di.Mock
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformState
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.domain.usecase.platform.GetRecentContentsUseCase
import com.example.likebox.presentation.view.screens.auth.state.SyncStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Mock private val platformRepository: PlatformRepository,
    private val getRecentContentsUseCase: GetRecentContentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                // 최근 동기화 시간 로드
                platformRepository.getLastSyncTime()
                    .onSuccess { timestamp ->
                        _uiState.update { state ->
                            state.copy(
                                lastSyncTime = LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(timestamp),
                                    ZoneId.systemDefault()
                                )
                            )
                        }
                    }

                // 플랫폼 상태 로드
                loadPlatformStates()

                // 최근 컨텐츠 로드
                loadRecentContents()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to load initial data")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun loadPlatformStates() {
        platformRepository.getPlatformSyncStatuses()
            .onSuccess { statuses ->
                val platformStates = MusicPlatform.entries.associateWith { platform ->
                    PlatformState(
                        platform = platform,
                        isEnabled = platform in listOf(
                            MusicPlatform.SPOTIFY,
                            MusicPlatform.APPLE_MUSIC,
                            MusicPlatform.YOUTUBE_MUSIC,
                            MusicPlatform.MELON
                        ),
                        isConnected = platformRepository.isPlatformConnected(platform)
                            .getOrDefault(false),
                        syncStatus = statuses[platform] ?: SyncStatus.NOT_SYNCED,
                        lastSyncTime = platformRepository.getLastSyncTime(platform)
                            .getOrNull(),
                        errorMessage = "Failed to load contents"
                    )
                }
                _uiState.update { it.copy(platformStates = platformStates) }
            }
    }

    private suspend fun loadRecentContents() {
        getRecentContentsUseCase()
            .onSuccess { contents ->
                _uiState.update { it.copy(recentContents = contents) }
            }
    }

    fun syncContent() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                platformRepository.syncAllPlatforms()
                    .onSuccess {
                        loadPlatformStates()
                        loadRecentContents()
                    }
                    .onFailure { error ->
                        _uiState.update {
                            it.copy(error = error.message ?: "Failed to sync content")
                        }
                    }

            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}