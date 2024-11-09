package com.example.likebox.data.firebase

import com.example.likebox.data.model.dto.TrackDto
import com.example.likebox.domain.model.MusicPlatform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseServiceImpl @Inject constructor() : FirebaseService {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override suspend fun getLikedTracks(platform: MusicPlatform): Result<List<TrackDto>> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Not authenticated"))

            val snapshot = db.collection("tracks")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("platform", platform.name)
                .get()
                .await()

            Result.success(snapshot.toObjects())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveLikedTrack(trackDto: TrackDto): Result<Unit> {
        TODO("Not yet implemented")
    }
}