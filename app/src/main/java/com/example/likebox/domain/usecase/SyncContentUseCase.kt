package com.example.likebox.domain.usecase

import com.example.likebox.domain.model.ContentType
import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.repository.MusicRepository
import javax.inject.Inject

class SyncContentUseCase @Inject constructor(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<Unit> {
        // Firebase에서 데이터 동기화 로직
        return repository.syncPlatformContent(platform, contentType)
    }
}