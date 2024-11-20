package com.example.likebox.data.repository

import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.repository.SearchRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : SearchRepository {

    override suspend fun search(query: String): Flow<List<MusicContent>> = flow {
        try {
            val snapshot = firestore.collection("content")
                .whereGreaterThanOrEqualTo("name", query)
                .whereLessThanOrEqualTo("name", query + '\uf8ff')
                .get()
                .await()

            val results = snapshot.documents.mapNotNull { doc ->
                when (doc.getString("type")) {
                    "track" -> doc.toObject(TrackDto::class.java)?.toDomain()
                    "album" -> doc.toObject(AlbumDto::class.java)?.toDomain()
                    "playlist" -> doc.toObject(PlaylistDto::class.java)?.toDomain()
                    else -> null
                }
            }

            emit(results)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getRecentSearches(): Flow<List<String>> = flow {
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("recent_searches")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            val searches = snapshot.documents.mapNotNull {
                it.getString("query")
            }

            emit(searches)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun addRecentSearch(query: String) {
        try {
            val userId = auth.currentUser?.uid ?: return

            firestore.collection("users")
                .document(userId)
                .collection("recent_searches")
                .add(mapOf(
                    "query" to query,
                    "timestamp" to com.google.firebase.Timestamp.now()
                ))
                .await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun removeRecentSearch(query: String) {
        try {
            val userId = auth.currentUser?.uid ?: return

            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("recent_searches")
                .whereEqualTo("query", query)
                .get()
                .await()

            snapshot.documents.forEach { doc ->
                doc.reference.delete().await()
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun clearRecentSearches() {
        try {
            val userId = auth.currentUser?.uid ?: return

            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("recent_searches")
                .get()
                .await()

            snapshot.documents.forEach { doc ->
                doc.reference.delete().await()
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    private data class TrackDto(
        val id: String = "",
        val type: String = "",
        val name: String = "",
        // ... other fields
    ) {
        fun toDomain(): MusicContent {
            // Convert DTO to domain model
            TODO("Implement conversion logic")
        }
    }

    private data class AlbumDto(
        val id: String = "",
        val type: String = "",
        val name: String = "",
        // ... other fields
    ) {
        fun toDomain(): MusicContent {
            // Convert DTO to domain model
            TODO("Implement conversion logic")
        }
    }

    private data class PlaylistDto(
        val id: String = "",
        val type: String = "",
        val name: String = "",
        // ... other fields
    ) {
        fun toDomain(): MusicContent {
            // Convert DTO to domain model
            TODO("Implement conversion logic")
        }
    }
}