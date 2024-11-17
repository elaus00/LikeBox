package com.example.likebox.data.repository.mock

import com.example.likebox.domain.model.*
import com.example.likebox.domain.model.library.ContentType
import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.settings.NotificationSettings
import com.example.likebox.domain.model.settings.SyncInterval
import com.example.likebox.domain.model.settings.SyncSettings
import com.example.likebox.domain.model.settings.ThemeSettings
import com.example.likebox.domain.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockSettingsRepositoryImpl @Inject constructor() : SettingsRepository {
    private var mockSettings = Settings(
        userId = "mock_user_id",
        theme = ThemeSettings(
            isDarkMode = false,
            useDynamicColor = true
        ),
        sync = SyncSettings(
            syncInterval = SyncInterval.DAILY,
            syncOnWifiOnly = true,
            syncContent = setOf(ContentType.TRACK, ContentType.PLAYLIST, ContentType.ALBUM),
            autoSync = true,
            platformSyncEnabled = mapOf(
                MusicPlatform.SPOTIFY to true,
                MusicPlatform.APPLE_MUSIC to false
            )
        ),
        notification = NotificationSettings(
            enabled = true,
            syncNotifications = true,
            contentUpdateNotifications = true
        ),
        language = "en"
    )

    override suspend fun getSettings(): Flow<Settings> = flow {
        delay(500) // 네트워크 지연 시뮬레이션
        emit(mockSettings)
    }

    override suspend fun updateSettings(settings: Settings): Result<Unit> {
        delay(500) // 네트워크 지연 시뮬레이션
        mockSettings = settings
        return Result.success(Unit)
    }

    override suspend fun exportUserData(): Result<String> {
        delay(1000) // 네트워크 지연 시뮬레이션
        return Result.success("""
            {
                "settings": ${mockSettings},
                "likedContent": [
                    {
                        "id": "track1",
                        "name": "Sample Track 1",
                        "artist": "Sample Artist 1"
                    },
                    {
                        "id": "track2",
                        "name": "Sample Track 2",
                        "artist": "Sample Artist 2"
                    }
                ]
            }
        """.trimIndent())
    }

    override suspend fun importUserData(jsonData: String): Result<Unit> {
        delay(1000) // 네트워크 지연 시뮬레이션
        return Result.success(Unit)
    }

    override suspend fun resetSettings(): Result<Unit> {
        delay(500) // 네트워크 지연 시뮬레이션
        mockSettings = Settings(userId = "mock_user_id")
        return Result.success(Unit)
    }
}