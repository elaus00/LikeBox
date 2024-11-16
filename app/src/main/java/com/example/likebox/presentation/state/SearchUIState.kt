package com.example.likebox.presentation.state

import com.example.likebox.domain.model.MusicContent

sealed class SearchUIState {
    object Initial : SearchUIState()
    object Loading : SearchUIState()
    data class RecentSearches(val searches: List<String>) : SearchUIState()
    data class Results(
        val query: String,
        val results: List<MusicContent>  // SearchResult -> MusicContent
    ) : SearchUIState()
    data class Error(val message: String) : SearchUIState()
}
