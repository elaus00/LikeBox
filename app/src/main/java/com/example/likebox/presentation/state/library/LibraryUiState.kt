package com.example.likebox.presentation.state.library

import com.example.likebox.domain.model.library.ContentType
import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.presentation.viewmodel.library.SortOrder

/**
 * 라이브러리 화면의 UI 상태를 정의하는 데이터 클래스
 *
 * @property selectedContentType 현재 선택된 컨텐츠 타입 (Track, Album, Playlist)
 * @property selectedPlatforms 선택된 음악 플랫폼들의 집합 (기본값: Spotify)
 * @property contents 현재 표시되는 음악 컨텐츠 리스트
 * @property isLoading 컨텐츠 로딩 중 여부
 * @property error 발생한 에러 메시지
 * @property showFilterSheet 필터 바텀시트의 표시 여부
 * @property sortOrder 현재 적용된 정렬 방식
 */
data class LibraryUiState(
    val selectedContentType: ContentType = ContentType.TRACK,
    val selectedPlatforms: Set<MusicPlatform> = setOf(MusicPlatform.SPOTIFY),
    val contents: List<MusicContent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showFilterSheet: Boolean = false,
    val sortOrder: SortOrder = SortOrder.LATEST
)