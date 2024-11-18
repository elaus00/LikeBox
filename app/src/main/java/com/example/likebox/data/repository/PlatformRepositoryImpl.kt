package com.example.likebox.data.repository

import com.example.likebox.data.firebase.FirebaseService
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformAuth
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.presentation.state.SyncStatus
import com.google.firebase.Firebase
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class PlatformRepositoryImpl @Inject constructor(
    private val firebaseService: FirebaseService,
    private val functions: FirebaseFunctions // 또는 필요한 다른 의존성
) : PlatformRepository {
    // Feat: getConnectedPlatforms
    override suspend fun getConnectedPlatforms(): Result<List<MusicPlatform>> {
        return try {
            val response = functions
                .getHttpsCallable("checkInfo")
                .call()
                .await()


            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch liked content"))
            }

            val item = result["data"] as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid track list format"))
            }

            val platformList = item["connetedPlatform"] as? List<String> ?: emptyList()
            val platforms = platformList.map{ MusicPlatform.fromId(it) }


            Result.success(platforms)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Feat: isPlatformConnected
    override suspend fun isPlatformConnected(platform: MusicPlatform): Result<Boolean> {
        return try {
            val data = mapOf(
                "platform" to platform.name
            )

            val response = functions
                .getHttpsCallable("verifyToken")
                .call(data)
                .await()


            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            val success = result["success"] as? Boolean ?: false

            if (!success) {
                return Result.failure(Exception("Failed to fetch liked content"))
            }

            else Result.success(true)


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
            val data = mapOf(
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
        return try {
            val data = mapOf(
                "platform" to platform.name
            )

            val response = functions
                .getHttpsCallable("verifyToken")
                .call(data)
                .await()


            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            val success = result["success"] as? Boolean ?: false

            if (!success) {
                return Result.failure(Exception("Failed to fetch liked content"))
            }

            else Result.success(PlatformAuth(platform, true))


        } catch (e: Exception) {
            Result.failure(e)
        }
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

    override suspend fun getLastSyncTime(): Result<Long> {
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
            val data = mapOf(
                "platform" to platform.name,
                "code" to authCode
            )

            val response = functions
                .getHttpsCallable("generateToken")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            val success = result["success"] as? Boolean ?: false

            if (!success) {
                return Result.failure(Exception("Failed to fetch liked content"))
            }

            else Result.success(PlatformAuth(platform, true))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logPlatformError(platform: MusicPlatform, error: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun syncAllPlatforms(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun syncPlatform(platform: MusicPlatform): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun observeSyncStatus(platform: MusicPlatform): Flow<SyncStatus> {
        TODO("Not yet implemented")
    }

    override suspend fun cancelSync(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSyncStatus(
        platform: MusicPlatform,
        status: SyncStatus
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlatformSyncStatuses(): Result<Map<MusicPlatform, SyncStatus>> {
        TODO("Not yet implemented")
    }
}