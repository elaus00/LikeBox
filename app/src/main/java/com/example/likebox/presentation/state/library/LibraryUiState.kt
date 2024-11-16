package com.example.likebox.presentation.state.library

import com.example.likebox.domain.model.*

/**
 * 라이브러리 화면의 UI 상태
 */
data class LibraryUiState(
    val selectedContentType: ContentType = ContentType.TRACK,
    val selectedPlatforms: Set<MusicPlatform> = setOf(MusicPlatform.SPOTIFY),
    val contents: List<MusicContent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)