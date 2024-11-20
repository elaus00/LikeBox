package com.example.likebox.presentation.view.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.usecase.search.SearchUseCase

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val searchState = _searchState.asStateFlow()

    private val _selectedPlatforms = MutableStateFlow<Set<MusicPlatform>>(emptySet())
    val selectedPlatforms = _selectedPlatforms.asStateFlow()

    init {
        loadRecentSearches()
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                loadRecentSearches()
            } else {
                performSearch(query)
            }
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            searchUseCase.getRecentSearches()
                .collect { searches ->
                    _searchState.value = SearchUiState.RecentSearches(searches)
                }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchUiState.Loading
            try {
                searchUseCase.search(query)
                    .collect { results ->
                        _searchState.value = SearchUiState.Results(
                            query = query,
                            results = results  // 이미 MusicContent 리스트임
                        )
                    }
            } catch (e: Exception) {
                _searchState.value = SearchUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun togglePlatform(platform: MusicPlatform) {
        _selectedPlatforms.value = _selectedPlatforms.value.toMutableSet().apply {
            if (contains(platform)) remove(platform) else add(platform)
        }
    }

    fun addToRecentSearches(query: String) {
        viewModelScope.launch {
            searchUseCase.addRecentSearch(query)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            searchUseCase.clearRecentSearches()
        }
    }

    fun removeRecentSearch(query: String) {
        viewModelScope.launch {
            searchUseCase.removeRecentSearch(query)
        }
    }
}