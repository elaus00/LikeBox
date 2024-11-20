package com.example.likebox.data.repository.mock

import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformAuth
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.presentation.view.screens.auth.state.SyncStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class MockPlatformRepositoryImpl @Inject constructor() : PlatformRepository {
    private val connectedPlatforms = mutableSetOf<MusicPlatform>()
    private val platformAuths = ConcurrentHashMap<MusicPlatform, PlatformAuth>()
    private val lastSyncTimes = ConcurrentHashMap<MusicPlatform, Long>()
    private val syncStatusFlows = ConcurrentHashMap<MusicPlatform, MutableStateFlow<SyncStatus>>()
    private var globalLastSyncTime: Long = System.currentTimeMillis()

    override suspend fun getConnectedPlatforms(): Result<List<MusicPlatform>> {
        delay(500)
        return Result.success(connectedPlatforms.toList())
    }

    override suspend fun isPlatformConnected(platform: MusicPlatform): Result<Boolean> {
        delay(300)
        return Result.success(platform in connectedPlatforms)
    }

    override suspend fun savePlatformAuth(platformAuth: PlatformAuth): Result<Unit> {
        delay(500)
        platformAuths[platformAuth.platform] = platformAuth
        if (platformAuth.isValid) {
            connectedPlatforms.add(platformAuth.platform)
        } else {
            connectedPlatforms.remove(platformAuth.platform)
        }
        return Result.success(Unit)
    }

    override suspend fun getPlatformAuth(platform: MusicPlatform): Result<PlatformAuth> {
        delay(300)
        return platformAuths[platform]?.let { Result.success(it) }
            ?: Result.failure(Exception("Platform auth not found"))
    }

    override suspend fun disconnectPlatform(platform: MusicPlatform): Result<Unit> {
        delay(500)
        connectedPlatforms.remove(platform)
        platformAuths.remove(platform)
        lastSyncTimes.remove(platform)
        syncStatusFlows.remove(platform)
        return Result.success(Unit)
    }

    override suspend fun refreshPlatformToken(platform: MusicPlatform): Result<PlatformAuth> {
        delay(1000)
        val newPlatformAuth = PlatformAuth(
            platform = platform,
            isValid = true
        )
        platformAuths[platform] = newPlatformAuth
        connectedPlatforms.add(platform)
        return Result.success(newPlatformAuth)
    }

    override suspend fun updateLastSyncTime(platform: MusicPlatform, timestamp: Long): Result<Unit> {
        lastSyncTimes[platform] = timestamp
        globalLastSyncTime = timestamp
        return Result.success(Unit)
    }

    override suspend fun getLastSyncTime(platform: MusicPlatform): Result<Long> {
        return Result.success(lastSyncTimes[platform] ?: 0L)
    }

    override suspend fun getLastSyncTime(): Result<Long> {
        return Result.success(globalLastSyncTime)
    }

    override suspend fun disconnectAllPlatforms(): Result<Unit> {
        delay(1000)
        connectedPlatforms.clear()
        platformAuths.clear()
        lastSyncTimes.clear()
        syncStatusFlows.clear()
        return Result.success(Unit)
    }

    override suspend fun connectPlatform(
        platform: MusicPlatform,
        authCode: String
    ): Result<PlatformAuth> {
        delay(1000)
        val platformAuth = PlatformAuth(
            platform = platform,
            isValid = true
        )
        platformAuths[platform] = platformAuth
        connectedPlatforms.add(platform)
        return Result.success(platformAuth)
    }

    override suspend fun logPlatformError(platform: MusicPlatform, error: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun syncAllPlatforms(): Result<Unit> {
        delay(2000)
        connectedPlatforms.forEach { platform ->
            updateSyncStatus(platform, SyncStatus.IN_PROGRESS)
            delay(500)
            updateSyncStatus(platform, SyncStatus.COMPLETED)
        }
        return Result.success(Unit)
    }

    override suspend fun syncPlatform(platform: MusicPlatform): Result<Unit> {
        delay(1000)
        updateSyncStatus(platform, SyncStatus.IN_PROGRESS)
        delay(500)
        updateSyncStatus(platform, SyncStatus.COMPLETED)
        return Result.success(Unit)
    }

    override fun observeSyncStatus(platform: MusicPlatform): Flow<SyncStatus> {
        return syncStatusFlows.getOrPut(platform) {
            MutableStateFlow(SyncStatus.IDLE)
        }.asStateFlow()
    }

    override suspend fun cancelSync(): Result<Unit> {
        delay(300)
        syncStatusFlows.values.forEach { it.value = SyncStatus.IDLE }
        return Result.success(Unit)
    }

    override suspend fun updateSyncStatus(
        platform: MusicPlatform,
        status: SyncStatus
    ): Result<Unit> {
        syncStatusFlows.getOrPut(platform) {
            MutableStateFlow(SyncStatus.IDLE)
        }.value = status
        return Result.success(Unit)
    }

    override suspend fun getPlatformSyncStatuses(): Result<Map<MusicPlatform, SyncStatus>> {
        delay(300)
        return Result.success(
            syncStatusFlows.mapValues { it.value.value }
        )
    }

    // 테스트 헬퍼 메서드들
    fun resetMockData() {
        connectedPlatforms.clear()
        platformAuths.clear()
        lastSyncTimes.clear()
        syncStatusFlows.clear()
        globalLastSyncTime = System.currentTimeMillis()
    }

    fun addMockPlatformAuth(platform: MusicPlatform) {
        val auth = PlatformAuth(platform = platform, isValid = true)
        platformAuths[platform] = auth
        connectedPlatforms.add(platform)
    }

    fun setMockSyncStatus(platform: MusicPlatform, status: SyncStatus) {
        syncStatusFlows.getOrPut(platform) {
            MutableStateFlow(SyncStatus.IDLE)
        }.value = status
    }
}