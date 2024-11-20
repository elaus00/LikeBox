package com.example.likebox.presentation.viewmodel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.Playlist
import com.example.likebox.domain.model.library.Track
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.presentation.view.screens.library.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistDetailState())
    val uiState: StateFlow<PlaylistDetailState> = _uiState.asStateFlow()

    fun loadPlaylist(playlistId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                musicRepository.getPlaylistById(playlistId).fold(
                    onSuccess = { playlist ->
                        _uiState.update {
                            it.copy(
                                playlist = playlist,
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = throwable.message
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun toggleLiked(content: MusicContent) {
        viewModelScope.launch {
            try {
                musicRepository.addToLiked(content).fold(
                    onSuccess = {
                        // 좋아요 상태 업데이트 성공
                    },
                    onFailure = { throwable ->
                        _uiState.update {
                            it.copy(error = throwable.message)
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message)
                }
            }
        }
    }

    fun addTracksToPlaylist(id: String, tracks: List<Track>) {
        TODO("Not yet implemented")
    }
}

data class PlaylistDetailState(
    val playlist: Playlist? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : DetailUiState