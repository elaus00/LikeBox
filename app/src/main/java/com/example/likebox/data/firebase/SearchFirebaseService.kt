package com.example.likebox.data.firebase

import com.example.likebox.domain.model.library.MusicContent

interface SearchFirebaseService {
    suspend fun searchContent(query: String): List<MusicContent>
    suspend fun getRecentSearches(userId: String): List<String>
    suspend fun addRecentSearch(userId: String, query: String)
    suspend fun removeRecentSearch(userId: String, query: String)
    suspend fun clearRecentSearches(userId: String)
}