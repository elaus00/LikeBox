package com.example.likebox.data.repository

import androidx.core.net.toUri
import com.example.likebox.domain.model.auth.User
import com.example.likebox.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserRepository {

    override suspend fun getCurrentUser(): Flow<User> = callbackFlow {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

        val listener = firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                snapshot?.toObject(User::class.java)?.let { user ->
                    trySend(user)
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun updateUser(user: User): Result<Unit> = try {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

        firestore.collection("users")
            .document(userId)
            .set(user)
            .await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateProfileImage(imageUri: String): Result<String> = try {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        val imageFileName = "profile_images/$userId/${UUID.randomUUID()}"

        // Upload image to Firebase Storage
        val storageRef = storage.reference.child(imageFileName)
        storageRef.putFile(imageUri.toUri()).await()

        // Get download URL
        val downloadUrl = storageRef.downloadUrl.await().toString()

        // Update user profile with new image URL
        firestore.collection("users")
            .document(userId)
            .update("profilePictureUrl", downloadUrl)
            .await()

        Result.success(downloadUrl)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteUser(): Result<Unit> = try {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

        // Delete user data from Firestore
        firestore.collection("users")
            .document(userId)
            .delete()
            .await()

        // Delete user authentication
        auth.currentUser?.delete()?.await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signOut(): Result<Unit> = try {
        auth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches()
    }
}