package com.example.likebox.domain.model.settings

import com.example.likebox.domain.model.library.ContentType
import com.example.likebox.domain.model.library.MusicPlatform

data class SyncSettings(
    val syncInterval: SyncInterval = SyncInterval.DAILY,
    val syncOnWifiOnly: Boolean = true,
    val syncContent: Set<ContentType> = setOf(ContentType.TRACK, ContentType.PLAYLIST, ContentType.ALBUM),
    val autoSync: Boolean = true,
    val platformSyncEnabled: Map<MusicPlatform, Boolean> = mapOf(
        MusicPlatform.SPOTIFY to true,
        MusicPlatform.APPLE_MUSIC to true
    )
)