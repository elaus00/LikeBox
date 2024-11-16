package com.example.likebox.presentation.state.library

import com.example.likebox.domain.model.*

/**
 * 아티스트 상세 화면의 UI 상태
 */
data class ArtistDetailUiState(
    val artist: Artist? = null,
    val topTracks: List<Track> = emptyList(),
    val albums: List<Album> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: String? = null
) : DetailUiState