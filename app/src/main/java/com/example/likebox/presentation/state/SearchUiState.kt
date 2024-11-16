package com.example.likebox.presentation.state

import com.example.likebox.domain.model.library.MusicContent

sealed class SearchUiState {
    object Initial : SearchUiState()
    object Loading : SearchUiState()
    data class RecentSearches(val searches: List<String>) : SearchUiState()
    data class Results(
        val query: String,
        val results: List<MusicContent>  // SearchResult -> MusicContent
    ) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}
