package com.example.likebox.presentation.viewmodel.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.model.library.ContentType
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.repository.MusicRepository
import com.example.likebox.presentation.state.library.LibraryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LibraryEvent {
    data class SelectContentType(val type: ContentType) : LibraryEvent
    data class TogglePlatform(val platform: MusicPlatform) : LibraryEvent
    data object RefreshContent : LibraryEvent
    data class UpdateSortOrder(val order: SortOrder) : LibraryEvent
}

enum class SortOrder {
    LATEST,
    NAME
}

/**
 * Library 화면의 UI 상태를 관리하는 ViewModel
 *
 * @property musicRepository 음악 관련 데이터를 가져오는 repository
 */
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    // UI 상태를 관리하는 MutableStateFlow
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        loadContent()  // ViewModel 초기화 시 컨텐츠 로드
    }

    /**
     * UI에서 발생하는 이벤트를 처리하는 함수
     *
     * @param event 처리할 LibraryEvent
     */
    fun onEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.SelectContentType -> {
                _uiState.update { it.copy(selectedContentType = event.type) }
                loadContent()  // 컨텐츠 타입 변경 시 새로운 컨텐츠 로드
            }
            is LibraryEvent.TogglePlatform -> {
                val currentPlatforms = _uiState.value.selectedPlatforms
                // 플랫폼이 이미 선택되어 있으면 제거, 없으면 추가
                val newPlatforms = if (event.platform in currentPlatforms) {
                    currentPlatforms - event.platform
                } else {
                    currentPlatforms + event.platform
                }
                _uiState.update { it.copy(selectedPlatforms = newPlatforms) }
                loadContent()  // 선택된 플랫폼 변경 시 새로운 컨텐츠 로드
            }
            is LibraryEvent.RefreshContent -> {
                loadContent()  // 컨텐츠 새로고침
            }
            is LibraryEvent.UpdateSortOrder -> {
                sortContent(event.order)  // 정렬 순서 변경
            }
        }
    }

    /**
     * 현재 선택된 컨텐츠 타입과 플랫폼에 따라 컨텐츠를 로드하는 함수
     * 로딩 상태를 관리하고 에러 처리도 함께 수행
     */
    private fun loadContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = when (_uiState.value.selectedContentType) {
                ContentType.TRACK -> musicRepository.getTracks(_uiState.value.selectedPlatforms)
                ContentType.ALBUM -> musicRepository.getAlbums(_uiState.value.selectedPlatforms)
                ContentType.PLAYLIST -> musicRepository.getPlaylists(_uiState.value.selectedPlatforms)
            }

            result.fold(
                onSuccess = { content ->
                    _uiState.update {
                        it.copy(
                            contents = content,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Unknown error occurred",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    /**
     * 현재 컨텐츠 리스트를 주어진 정렬 순서에 따라 정렬하는 함수
     *
     * @param order 적용할 정렬 순서 (최신순 or 이름순)
     */
    private fun sortContent(order: SortOrder) {
        val sortedContent = when (order) {
            SortOrder.LATEST -> _uiState.value.contents.sortedByDescending { it.createdAt }
            SortOrder.NAME -> _uiState.value.contents.sortedBy { it.name }
        }
        _uiState.update { it.copy(contents = sortedContent) }
    }
}