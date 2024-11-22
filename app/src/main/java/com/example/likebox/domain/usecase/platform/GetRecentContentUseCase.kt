package com.example.likebox.domain.usecase.platform

import com.example.likebox.di.Mock
import com.example.likebox.domain.model.library.MusicContent
import com.example.likebox.domain.repository.ContentRepository
import com.example.likebox.domain.repository.PlatformRepository
import com.example.likebox.presentation.view.screens.auth.state.SyncStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRecentContentsUseCase @Inject constructor(
    @Mock private val platformRepository: PlatformRepository,
    private val contentRepository: ContentRepository
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<MusicContent>> = withContext(
        Dispatchers.IO) {
        try {
            // 1. 연결된 플랫폼 확인
            val connectedPlatforms = platformRepository.getConnectedPlatforms()
                .getOrDefault(emptyList())

            if (connectedPlatforms.isEmpty()) {
                return@withContext Result.success(emptyList())
            }

            // 2. 각 플랫폼의 동기화 상태 확인
            val syncStatuses = platformRepository.getPlatformSyncStatuses()
                .getOrDefault(emptyMap())

            // 3. 동기화가 완료된 플랫폼만 필터링
            val syncedPlatforms = connectedPlatforms.filter { platform ->
                syncStatuses[platform] == SyncStatus.COMPLETED
            }

            if (syncedPlatforms.isEmpty()) {
                return@withContext Result.success(emptyList())
            }

            // 4. 각 플랫폼별로 최근 컨텐츠 조회 및 통합
            val recentContents = syncedPlatforms
                .mapNotNull { platform ->
                    contentRepository.getRecentContents(
                        platform = platform,
                        limit = limit
                    ).getOrNull()
                }
                .fold(mutableListOf<MusicContent>()) { acc, list ->
                    acc.apply { addAll(list) }
                }
                .sortedWith(compareByDescending { it.updatedAt })
                .take(limit)

            Result.success(recentContents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}