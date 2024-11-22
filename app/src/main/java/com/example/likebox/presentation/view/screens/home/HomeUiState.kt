package com.example.likebox.presentation.view.screens.home

import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformState
import java.time.LocalDateTime

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val platformStates: Map<MusicPlatform, PlatformState> = emptyMap(),
    val recentContents: List<MusicContent> = emptyList(),
    val lastSyncTime: LocalDateTime? = null
)
