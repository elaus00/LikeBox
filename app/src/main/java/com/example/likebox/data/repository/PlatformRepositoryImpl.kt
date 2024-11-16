package com.example.likebox.data.repository

import com.example.likebox.data.firebase.FirebaseService
import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.model.PlatformAuth
import com.example.likebox.domain.repository.PlatformRepository
import com.google.firebase.Firebase
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class PlatformRepositoryImpl @Inject constructor(
    private val firebaseService: FirebaseService,
    private val functions: FirebaseFunctions// 또는 필요한 다른 의존성
) : PlatformRepository {
    // Feat: getConnectedPlatforms
    override suspend fun getConnectedPlatforms(): Result<List<MusicPlatform>> {
        return try {
            val result = functions
                .getHttpsCallable("checkInfo")
                .call()
                .await()

            val responseData = result.getData() as Map<String, Any>
            val data = responseData["data"] as Map<String, Any>
            val platforms = (data["connectedPlatforms"] as List<String>)
                .map { MusicPlatform.fromId(it) }

            Result.success(platforms)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Feat: isPlatformConnected
    override suspend fun isPlatformConnected(platform: MusicPlatform): Result<Boolean> {
        return try {
            val data = hashMapOf(
                "platform" to platform.name
            )

            val result = functions
                .getHttpsCallable("verifyToken")
                .call(data)
                .await()

            val responseData = result.getData() as Map<String, Any>
            val success = responseData["success"] as Boolean

            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun savePlatformAuth(platformAuth: PlatformAuth): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlatformAuth(platform: MusicPlatform): Result<PlatformAuth> {
        TODO("Not yet implemented")
    }

    // Feat: disconnectPlatform
    override suspend fun disconnectPlatform(platform: MusicPlatform): Result<Unit> {
        return try {
            val data = hashMapOf(
                "platform" to platform.name
            )

            functions
                .getHttpsCallable("removeToken")
                .call(data)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshPlatformToken(platform: MusicPlatform): Result<PlatformAuth> {
        TODO("Not yet implemented")
    }

    override suspend fun updateLastSyncTime(
        platform: MusicPlatform,
        timestamp: Long
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getLastSyncTime(platform: MusicPlatform): Result<Long> {
        TODO("Not yet implemented")
    }

    //Feat: disconnectAllPlatforms
    override suspend fun disconnectAllPlatforms(): Result<Unit> {
        return try {
            functions
                .getHttpsCallable("removeAllTokens")
                .call()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Feat: connectPlatform
    override suspend fun connectPlatform(
        platform: MusicPlatform,
        authCode: String
    ): Result<PlatformAuth> {
        return try {
            val data = hashMapOf(
                "platform" to platform.name,
                "code" to authCode
            )

            functions
                .getHttpsCallable("generateToken")
                .call(data)
                .await()

            Result.success(
                PlatformAuth(
                    platform = platform,
                    isValid = true
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logPlatformError(platform: MusicPlatform, error: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}