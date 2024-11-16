package com.example.likebox.presentation.viewmodel.library

import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.presentation.state.library.ArtistDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
* 아티스트 상세 화면의 ViewModel
*/
@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : BaseDetailViewModel() {
    private val _uiState = MutableStateFlow(ArtistDetailUiState())
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()

    fun loadArtist(artistId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 병렬로 아티스트 정보, 트랙, 앨범을 로드
            launch {
                musicRepository.getArtistById(artistId)
                    .onSuccess { artist ->
                        _uiState.update { it.copy(artist = artist) }
                    }
                    .onFailure { error ->
                        _uiState.update {
                            it.copy(error = handleError(error))
                        }
                    }
            }

            launch {
                musicRepository.getArtistTracks(artistId)
                    .onSuccess { tracks ->
                        _uiState.update { it.copy(topTracks = tracks) }
                    }
            }

            launch {
                musicRepository.getArtistAlbums(artistId)
                    .onSuccess { albums ->
                        _uiState.update { it.copy(albums = albums) }
                    }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}