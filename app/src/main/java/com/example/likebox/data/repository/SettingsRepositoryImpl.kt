package com.example.likebox.data.repository

import com.example.likebox.data.firebase.FirebaseService
import com.example.likebox.domain.model.Settings
import com.example.likebox.domain.repository.SettingsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseService: FirebaseService
) : SettingsRepository {

    private val gson = Gson()

    override suspend fun getSettings(): Flow<Settings> = callbackFlow {
        val listener = firestore.collection("settings")
            .document(getCurrentUserId())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                snapshot?.toObject(Settings::class.java)?.let { settings ->
                    trySend(settings)
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun updateSettings(settings: Settings): Result<Unit> = try {
        firestore.collection("settings")
            .document(getCurrentUserId())
            .set(settings)
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun exportUserData(): Result<String> = try {
        val userData = mutableMapOf<String, Any>()

        // Collect all user data
        val settings = firestore.collection("settings")
            .document(getCurrentUserId())
            .get()
            .await()
            .toObject(Settings::class.java)

        val likedTracks = firebaseService.getLikedTracks(platform = null)

        userData["settings"] = settings ?: Settings(getCurrentUserId())
        userData["likedContent"] = likedTracks

        Result.success(gson.toJson(userData))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun importUserData(jsonData: String): Result<Unit> = try {
        val userData = gson.fromJson(jsonData, Map::class.java)

        // Validate data before importing
        validateUserData(userData)

        // Import settings
        val settings = gson.fromJson(gson.toJson(userData["settings"]), Settings::class.java)
        updateSettings(settings)

        // Import liked content
        val likedContent = userData["likedContent"] as? List<*>
        likedContent?.forEach { content ->
            // Import each content item
            // Implementation depends on your data structure
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun resetSettings(): Result<Unit> = try {
        updateSettings(Settings(getCurrentUserId()))
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun getCurrentUserId(): String {
        // Implement getting current user ID from FirebaseAuth
        return FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")
    }

    private fun validateUserData(userData: Map<*, *>) {
        require(userData.containsKey("settings")) { "Invalid data: missing settings" }
        require(userData.containsKey("likedContent")) { "Invalid data: missing liked content" }
    }
}