package com.example.likebox.domain.usecase.platform

import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.repository.PlatformRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncPlatformUseCase @Inject constructor(
    private val platformRepository: PlatformRepository
) {
    suspend operator fun invoke(platform: MusicPlatform): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            platformRepository.syncPlatform(platform)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}