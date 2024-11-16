package com.example.likebox.presentation.state.library

import com.example.likebox.domain.model.*

/**
 * 플레이리스트 상세 화면의 UI 상태
 */
data class PlaylistDetailUiState(
    val playlist: Playlist? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : DetailUiState
