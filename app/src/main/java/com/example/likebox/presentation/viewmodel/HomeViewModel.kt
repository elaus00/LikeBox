package com.example.likebox.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.di.Mock
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.presentation.state.HomeUiState
import com.example.likebox.presentation.state.PlatformSyncStatus
import com.example.likebox.presentation.state.SyncStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Mock private val musicRepository: MusicRepository,
    @Mock private val platformRepository: PlatformRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // 연결된 플랫폼 상태 로드
                // Result로 되어있는 것 가져올 때는 get 메서드 사용해서 안의 리스트 추출해야함. 단, Null safety를 위해 getOrNull 메서드 사용.
                val platforms = platformRepository.getConnectedPlatforms().getOrNull() ?: emptyList()
                val platformStatuses = platforms.associateWith { platform ->
                    PlatformSyncStatus(
                        isConnected = true,
                        syncStatus = SyncStatus.COMPLETED
                    )
                }

                // 최근 콘텐츠 로드
                val recentContents = musicRepository.getRecentContents()

                // 마지막 동기화 시간 로드
                val lastSync = platformRepository.getLastSyncTime()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    recentContents = recentContents,
                    platformStatuses = platformStatuses,
                    lastSyncTime = LocalDateTime.ofInstant(
                        lastSync.getOrNull()?.let { Instant.ofEpochMilli(it) },
                        ZoneId.systemDefault()
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun syncContent() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    platformStatuses = _uiState.value.platformStatuses.mapValues {
                        it.value.copy(syncStatus = SyncStatus.IN_PROGRESS)
                    }
                )

                // 각 플랫폼 동기화 실행
                platformRepository.syncAllPlatforms()

                // 동기화 후 데이터 새로고침
                loadInitialData()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    platformStatuses = _uiState.value.platformStatuses.mapValues {
                        it.value.copy(syncStatus = SyncStatus.ERROR)
                    },
                    error = e.message
                )
            }
        }
    }

    fun formatLastSyncTime(lastSyncTime: LocalDateTime?): String {
        if (lastSyncTime == null) return "Not synced yet"

        val now = LocalDateTime.now()
        val minutes = ChronoUnit.MINUTES.between(lastSyncTime, now)

        return when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes minutes ago"
            minutes < 1440 -> "${minutes / 60} hours ago"
            else -> "${minutes / 1440} days ago"
        }
    }
}