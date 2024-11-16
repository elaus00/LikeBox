package com.example.likebox.data.repository

import com.example.likebox.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.google.firebase.auth.FirebaseAuth
import com.example.likebox.data.firebase.SearchFirebaseService
import com.example.likebox.domain.model.library.MusicContent
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchService: SearchFirebaseService,
    private val auth: FirebaseAuth
) : SearchRepository {
    override suspend fun search(query: String): Flow<List<MusicContent>> = flow {
        emit(searchService.searchContent(query))
    }

    override suspend fun getRecentSearches(): Flow<List<String>> = flow {
        auth.currentUser?.uid?.let { userId ->
            emit(searchService.getRecentSearches(userId))
        } ?: emit(emptyList())
    }

    override suspend fun addRecentSearch(query: String) {
        auth.currentUser?.uid?.let { userId ->
            searchService.addRecentSearch(userId, query)
        }
    }

    override suspend fun removeRecentSearch(query: String) {
        auth.currentUser?.uid?.let { userId ->
            searchService.removeRecentSearch(userId, query)
        }
    }

    override suspend fun clearRecentSearches() {
        auth.currentUser?.uid?.let { userId ->
            searchService.clearRecentSearches(userId)
        }
    }
}