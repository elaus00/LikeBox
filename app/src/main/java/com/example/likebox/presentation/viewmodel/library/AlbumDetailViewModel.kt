package com.example.likebox.presentation.viewmodel.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.model.Album
import com.example.likebox.domain.model.MusicContent
import com.example.likebox.domain.model.Track
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.presentation.state.library.AlbumDetailUiState
import com.example.likebox.presentation.state.library.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 앨범 상세 화면의 ViewModel
 */
@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumDetailState())
    val uiState: StateFlow<AlbumDetailState> = _uiState.asStateFlow()


    fun loadAlbum(albumId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // 앨범 정보 로드
                musicRepository.getAlbumById(albumId).fold(
                    onSuccess = { album ->
                        // 앨범의 트랙 정보 로드
                        musicRepository.getArtistTracks(album.artists.first()).fold(
                            onSuccess = { tracks ->
                                _uiState.update {
                                    it.copy(
                                        album = album,
                                        tracks = tracks,
                                        isLoading = false,
                                        error = null
                                    )
                                }
                            },
                            onFailure = { throwable ->
                                _uiState.update {
                                    it.copy(
                                        album = album,
                                        isLoading = false,
                                        error = throwable.message
                                    )
                                }
                            }
                        )
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
}

data class AlbumDetailState(
    val album: Album? = null,
    val tracks : List<Track> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: String? = null
) : DetailUiState