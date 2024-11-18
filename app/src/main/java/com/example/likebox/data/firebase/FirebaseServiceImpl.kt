package com.example.likebox.data.firebase

import com.example.likebox.data.model.dto.TrackDtoFs
import com.example.likebox.domain.model.MusicPlatform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : FirebaseService {

    override suspend fun getLikedTracks(platform: MusicPlatform): Result<List<TrackDtoFs>> {
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

    override suspend fun saveLikedTrack(trackDto: TrackDtoFs): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Not authenticated"))

            trackDto.id = userId

            db.collection("tracks")
                .add(trackDto)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConnectedPlatforms(): List<MusicPlatform> {
        TODO("Not yet implemented")
    }
}