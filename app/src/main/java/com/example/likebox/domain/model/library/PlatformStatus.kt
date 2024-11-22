package com.example.likebox.domain.model.library

import com.example.likebox.presentation.view.screens.auth.state.SyncStatus

data class PlatformState(
    val platform: MusicPlatform,
    val isEnabled: Boolean,
    val isConnected: Boolean,
    val syncStatus: SyncStatus,
    val lastSyncTime: Long?,
    val errorMessage: String?
) {
    companion object {
        fun default(platform: MusicPlatform) = PlatformState(
            platform = platform,
            isEnabled = when (platform) {
                MusicPlatform.SPOTIFY,
                MusicPlatform.APPLE_MUSIC,
                MusicPlatform.YOUTUBE_MUSIC,
                MusicPlatform.MELON -> true
                else -> false
            },
            isConnected = false,
            syncStatus = SyncStatus.IDLE,
            lastSyncTime = null,
            errorMessage = null
        )
    }
}