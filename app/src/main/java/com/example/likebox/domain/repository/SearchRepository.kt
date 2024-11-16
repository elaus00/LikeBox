package com.example.likebox.domain.repository

import android.app.appsearch.SearchResult
import com.example.likebox.domain.model.MusicContent
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun search(query: String): Flow<List<MusicContent>>
    suspend fun getRecentSearches(): Flow<List<String>>
    suspend fun addRecentSearch(query: String)
    suspend fun removeRecentSearch(query: String)
    suspend fun clearRecentSearches()
}