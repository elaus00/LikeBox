package com.example.likebox.presentation.state.library

import com.example.likebox.domain.model.library.Track

/**
 * 아티스트 상세 화면의 UI 상태
 */
data class TrackDetailState(
    val track: Track? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null
) :DetailUiState