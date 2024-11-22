package com.example.likebox.data.repository

import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformAuth
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.presentation.view.screens.auth.state.SyncStatus
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class PlatformRepositoryImpl @Inject constructor(
    private val functions: FirebaseFunctions// 또는 필요한 다른 의존성
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

                if (result["success"] == false) {
                    return Result.failure(Exception("Failed to fetch platform tracks"))
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

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch platform tracks"))
            }

            else Result.success(PlatformAuth(platform, true))


        } catch (e: Exception) {
            Result.failure(e)
        }
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
                "authCode" to authCode
            )

            val response = functions
                .getHttpsCallable("generateToken")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            Result.success(PlatformAuth(platform, true))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateLastSyncTime(platform: MusicPlatform, timestamp: Long): Result<Unit> {
        return try {
            val data = hashMapOf(
                "platform" to platform.name,
                "timestamp" to timestamp
            )

            functions
                .getHttpsCallable("updateSyncTime")
                .call(data)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLastSyncTime(platform: MusicPlatform): Result<Long> {
        return try {
            val data = hashMapOf(
                "platform" to platform.name
            )

            val result = functions
                .getHttpsCallable("getLastSyncTime")
                .call(data)
                .await()

            val responseData = result.getData() as Map<String, Any>
            val timestamp = responseData["timestamp"] as Long

            Result.success(timestamp)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLastSyncTime(): Result<Long> {
        return try {
            val result = functions
                .getHttpsCallable("getLastGlobalSyncTime")
                .call()
                .await()

            val responseData = result.getData() as Map<String, Any>
            val timestamp = responseData["timestamp"] as Long

            Result.success(timestamp)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncPlatform(platform: MusicPlatform): Result<Unit> {
        return try {
            val data = hashMapOf(
                "platform" to platform.name
            )

            functions
                .getHttpsCallable("syncPlatform")
                .call(data)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSyncStatus(
        platform: MusicPlatform,
        status: SyncStatus
    ): Result<Unit> {
        return try {
            val data = hashMapOf(
                "platform" to platform.name,
                "status" to status.name
            )

            functions
                .getHttpsCallable("updateSyncStatus")
                .call(data)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlatformSyncStatuses(): Result<Map<MusicPlatform, SyncStatus>> {
        return try {
            val result = functions
                .getHttpsCallable("getSyncStatuses")
                .call()
                .await()

            // 디버깅을 위한 로그
            println("Raw response: ${result.getData()}")

            // 응답 데이터 구조 확인
            val responseData = when (val data = result.getData()) {
                is Map<*, *> -> data as? Map<String, String>
                is ArrayList<*> -> {
                    // ArrayList를 Map으로 변환하는 로직
                    data.associate { item ->
                        when (item) {
                            is Map<*, *> -> {
                                val platform = item["platform"] as? String ?: ""
                                val status = item["status"] as? String ?: ""
                                platform to status
                            }
                            else -> "" to ""
                        }
                    }
                }
                else -> mapOf()
            } ?: mapOf()

            val statusMap = responseData.filterKeys { it.isNotEmpty() }
                .mapKeys { MusicPlatform.fromId(it.key) }
                .mapValues { SyncStatus.valueOf(it.value) }

            Result.success(statusMap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Flow를 통한 실시간 상태 감지
    override fun observeSyncStatus(platform: MusicPlatform): Flow<SyncStatus> = callbackFlow {
        // 실제 구현에서는 Firestore의 실시간 업데이트나 다른 메커니즘을 사용해야 함
        // 여기서는 일단 TODO로 남겨둡니다.
        TODO("Need to implement real-time sync status observation")
    }

    override suspend fun cancelSync(): Result<Unit> {
        return try {
            functions
                .getHttpsCallable("cancelSync")
                .call()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logPlatformError(platform: MusicPlatform, error: String): Result<Unit> {
        return try {
            val data = hashMapOf(
                "platform" to platform.name,
                "error" to error
            )

            functions
                .getHttpsCallable("logError")
                .call(data)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncAllPlatforms(): Result<Unit> {
        return try {
            functions
                .getHttpsCallable("syncAllPlatforms")
                .call()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}