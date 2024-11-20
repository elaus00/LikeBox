package com.example.likebox.presentation.view.screens.home

import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.presentation.view.screens.auth.state.PlatformSyncStatus
import java.time.LocalDateTime

data class HomeUiState(
    val isLoading: Boolean = false,
    val recentContents: List<MusicContent> = emptyList(),
    val platformStatuses: Map<MusicPlatform, PlatformSyncStatus> = emptyMap(),
    val lastSyncTime: LocalDateTime? = null,
    val error: String? = null
)