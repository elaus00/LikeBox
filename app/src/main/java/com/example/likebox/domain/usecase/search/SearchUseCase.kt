package com.example.likebox.domain.usecase.search

import com.example.likebox.domain.model.MusicContent
import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val authRepository: AuthRepository
) {
    suspend fun search(query: String): StateFlow<List<MusicContent>> =
        searchRepository.search(query)
            .catch {
                // Log error if needed
                emit(emptyList())
            }
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    suspend fun getRecentSearches(): Flow<List<String>> =
        searchRepository.getRecentSearches()
            .catch {
                // Log error if needed
                emit(emptyList())
            }

    suspend fun addRecentSearch(query: String) {
        try {
            searchRepository.addRecentSearch(query)
        } catch (e: Exception) {
            // Handle error - maybe throw custom exception or log
        }
    }

    suspend fun removeRecentSearch(query: String) {
        try {
            searchRepository.removeRecentSearch(query)
        } catch (e: Exception) {
            // Handle error - maybe throw custom exception or log
        }
    }

    suspend fun clearRecentSearches() {
        try {
            searchRepository.clearRecentSearches()
        } catch (e: Exception) {
            // Handle error - maybe throw custom exception or log
        }
    }
}