package com.example.likebox.presentation.view.screens.library.state

import com.example.likebox.domain.model.library.Album
import com.example.likebox.domain.model.library.Artist
import com.example.likebox.domain.model.library.Track

/**
 * 아티스트 상세 화면의 UI 상태
 */
data class ArtistDetailState(
    val artist: Artist? = null,
    val topTracks: List<Track> = emptyList(),
    val albums: List<Album> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: String? = null
) : DetailUiState