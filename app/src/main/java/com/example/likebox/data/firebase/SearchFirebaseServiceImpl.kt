package com.example.likebox.data.firebase

import com.example.likebox.data.model.dto.AlbumDto
import com.example.likebox.data.model.dto.PlaylistDto
import com.example.likebox.data.model.dto.TrackDto
import com.example.likebox.data.model.dto.TrackMapper.toDomain
import com.example.likebox.domain.model.library.MusicContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SearchFirebaseServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SearchFirebaseService {

    override suspend fun searchContent(query: String): List<MusicContent> = suspendCoroutine { continuation ->
        firestore.collection("content")
            .whereGreaterThanOrEqualTo("name", query)
            .whereLessThanOrEqualTo("name", query + '\uf8ff')
            .get()
            .addOnSuccessListener { snapshot ->
                val results = snapshot.documents.mapNotNull { doc ->
                    when (doc.getString("type")) {
                        "track" -> doc.toObject<TrackDto>()?.toDomain()
                        "album" -> doc.toObject<AlbumDto<Any?>>()?.toDomain()
                        "playlist" -> doc.toObject<PlaylistDto<Any?>>()?.toDomain()
                        else -> null
                    }
                }
                continuation.resume(results as List<MusicContent>)
            }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    override suspend fun getRecentSearches(userId: String): List<String> = suspendCoroutine { continuation ->
        firestore.collection("users")
            .document(userId)
            .collection("recent_searches")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { snapshot ->
                val searches = snapshot.documents.mapNotNull { it.getString("query") }
                continuation.resume(searches)
            }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    override suspend fun addRecentSearch(userId: String, query: String) {
        firestore.collection("users")
            .document(userId)
            .collection("recent_searches")
            .add(mapOf(
                "query" to query,
                "timestamp" to FieldValue.serverTimestamp()
            ))
    }

    override suspend fun removeRecentSearch(userId: String, query: String) {
        firestore.collection("users")
            .document(userId)
            .collection("recent_searches")
            .whereEqualTo("query", query)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.documents.forEach { it.reference.delete() }
            }
    }

    override suspend fun clearRecentSearches(userId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("recent_searches")
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.documents.forEach { it.reference.delete() }
            }
    }
}