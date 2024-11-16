package com.example.likebox.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.usecase.search.SearchUseCase
import com.example.likebox.presentation.state.SearchUIState

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    private val _searchState = MutableStateFlow<SearchUIState>(SearchUIState.Initial)
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
                    _searchState.value = SearchUIState.RecentSearches(searches)
                }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchUIState.Loading
            try {
                searchUseCase.search(query)
                    .collect { results ->
                        _searchState.value = SearchUIState.Results(
                            query = query,
                            results = results  // 이미 MusicContent 리스트임
                        )
                    }
            } catch (e: Exception) {
                _searchState.value = SearchUIState.Error(e.message ?: "Unknown error occurred")
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