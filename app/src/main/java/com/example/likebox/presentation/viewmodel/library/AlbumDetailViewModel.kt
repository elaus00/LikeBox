package com.example.likebox.presentation.viewmodel.library

import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.presentation.state.library.AlbumDetailUiState
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
) : BaseDetailViewModel() {
    private val _uiState = MutableStateFlow(AlbumDetailUiState())
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    fun loadAlbum(albumId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            musicRepository.getAlbumById(albumId)
                .onSuccess { album ->
                    _uiState.update {
                        it.copy(
                            album = album,
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