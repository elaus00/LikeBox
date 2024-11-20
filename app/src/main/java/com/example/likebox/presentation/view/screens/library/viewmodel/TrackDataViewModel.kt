package com.example.likebox.presentation.view.screens.library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.Track
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.presentation.view.screens.library.state.DetailUiState
import com.example.likebox.presentation.view.screens.library.state.TrackDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackDetailViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackDetailState())
    val uiState: StateFlow<TrackDetailState> = _uiState.asStateFlow()

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

    fun removeLiked(content: MusicContent) {
        viewModelScope.launch {
            try {
                musicRepository.removeFromLiked(content).fold(
                    onSuccess = {
                        // 좋아요 제거 성공
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

data class TrackDetailState(
    val track: Track? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : DetailUiState