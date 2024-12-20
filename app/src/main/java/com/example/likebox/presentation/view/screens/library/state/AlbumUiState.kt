package com.example.likebox.presentation.view.screens.library.state

import com.example.likebox.domain.model.library.Album
import com.example.likebox.domain.model.library.Track

/**
 * 앨범 상세 화면의 UI 상태
 */
data class AlbumDetailUiState(
    val album: Album? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val tracks: List<Track> = emptyList()
) : DetailUiState