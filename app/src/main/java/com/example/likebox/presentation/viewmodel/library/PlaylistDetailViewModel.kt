package com.example.likebox.presentation.viewmodel.library

import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.presentation.state.library.PlaylistDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 플레이리스트 상세 화면의 ViewModel
 */
@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : BaseDetailViewModel() {
    private val _uiState = MutableStateFlow(PlaylistDetailUiState())
    val uiState: StateFlow<PlaylistDetailUiState> = _uiState.asStateFlow()

    fun loadPlaylist(playlistId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            musicRepository.getPlaylistById(playlistId)
                .onSuccess { playlist ->
                    _uiState.update {
                        it.copy(
                            playlist = playlist,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            error = handleError(error),
                            isLoading = false
                        )
                    }
                }
        }
    }
}